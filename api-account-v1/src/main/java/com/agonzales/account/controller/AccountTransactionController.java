package com.agonzales.account.controller;

import com.agonzales.account.dto.controller.accounttransaction.AccountResponse;
import com.agonzales.account.dto.controller.accounttransaction.AccountTransactionResponse;
import com.agonzales.account.dto.domain.Transaction;
import com.agonzales.account.service.TransactionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/cuentas/{accountId}/movimientos")
public class AccountTransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<AccountTransactionResponse> getTransactionsByAccountId(@PathVariable Integer accountId) {
        return transactionService.getByAccountId(accountId).stream()
          .map(this::convertDomainToResponse)
          .collect(Collectors.toList());
    }

    @GetMapping("/{transactionId}")
    public AccountTransactionResponse getTransactionById(@PathVariable Integer accountId, @PathVariable Integer transactionId) {
        return convertDomainToResponse(transactionService.getByAccountIdAndTransactionId(accountId, transactionId));
    }

    private AccountTransactionResponse convertDomainToResponse(Transaction transaction) {
        AccountTransactionResponse accountTransactionResponse = new AccountTransactionResponse();
        accountTransactionResponse.setId(transaction.getId());
        accountTransactionResponse.setType(transaction.getTransactionType());
        accountTransactionResponse.setAmount(transaction.getAmount());
        accountTransactionResponse.setBalance(transaction.getBalance());
        accountTransactionResponse.setDate(transaction.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(transaction.getAccount().getId());
        accountResponse.setAccountNumber(transaction.getAccount().getAccountNumber());

        accountTransactionResponse.setAccount(accountResponse);

        return accountTransactionResponse;
    }
}
