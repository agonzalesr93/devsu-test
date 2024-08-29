package com.agonzales.account.service;

import com.agonzales.account.config.kafka.ClientInfoMessage;
import com.agonzales.account.dao.AccountDao;
import com.agonzales.account.dao.TransactionDao;
import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.AccountStatementLine;
import com.agonzales.account.dto.domain.Client;
import com.agonzales.account.dto.domain.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@AllArgsConstructor
public class ReportService {

    private static final String CLIENT_INFO_GROUP_ID       = "client-info-group-id";
    private static final String CLIENT_INFO_RESPONSE_TOPIC = "client-info-response-topic";
    private static final String CLIENT_INFO_REQUEST_TOPIC  = "client-info-request-topic";

    private final ConcurrentMap<String, CompletableFuture<ClientInfoMessage>> pendingRequests = new ConcurrentHashMap<>();

    private KafkaTemplate<String, String> kafkaTemplate;
    private AccountDao                    accountDao;
    private TransactionDao                transactionDao;
    private ObjectMapper                  objectMapper;

    public CompletableFuture<ClientInfoMessage> sendMessageAndWaitForResponse(ClientInfoMessage message) {
        CompletableFuture<ClientInfoMessage> future = new CompletableFuture<>();
        pendingRequests.put(message.getId().toString(), future);

        try {
            kafkaTemplate.send(CLIENT_INFO_REQUEST_TOPIC, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return future;
    }

    @KafkaListener(topics = CLIENT_INFO_RESPONSE_TOPIC, groupId = CLIENT_INFO_GROUP_ID)
    public void listenForClientInfoResponse(String response) {

        try {
            ClientInfoMessage clientInfoMessage = objectMapper.readValue(response, ClientInfoMessage.class);

            CompletableFuture<ClientInfoMessage> future = pendingRequests.remove(clientInfoMessage.getId().toString());
            if (future != null) {
                future.complete(clientInfoMessage);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Page<AccountStatementLine> generateAccountStatement(LocalDate dateFrom, LocalDate dateTo, Integer clientId, Pageable pageable) {
        ClientInfoMessage clientInfo = new ClientInfoMessage();
        clientInfo.setId(UUID.randomUUID());
        clientInfo.setClientId(clientId);

        CompletableFuture<ClientInfoMessage> clientInfoFuture = sendMessageAndWaitForResponse(clientInfo);

        ClientInfoMessage clientInfoResponse = null;
        try {
            clientInfoResponse = clientInfoFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        Client client = new Client();
        client.setId(clientInfoResponse.getClientId());
        client.setName(clientInfoResponse.getName());

        List<Integer> accountIds = accountDao.getAllByClientId(clientId)
          .stream()
          .map(Account::getId)
          .toList();

        Page<Transaction> transactionsPage = transactionDao.getTransactionsBy(accountIds, dateFrom, dateTo, pageable);

        List<AccountStatementLine> accountStatementLines = transactionsPage.getContent()
          .stream()
          .map(transaction -> buildAccountStatementLine(client, transaction.getAccount(), transaction))
          .toList();

        Pageable pageableResponse = transactionsPage.getPageable();
        return new PageImpl<>(accountStatementLines, pageableResponse, transactionsPage.getTotalElements());
    }

    private AccountStatementLine buildAccountStatementLine(Client client, Account account, Transaction transaction) {
        AccountStatementLine accountStatementLine = new AccountStatementLine();

        accountStatementLine.setClient(client);
        accountStatementLine.setAccount(account);
        accountStatementLine.setTransaction(transaction);

        return accountStatementLine;
    }
}
