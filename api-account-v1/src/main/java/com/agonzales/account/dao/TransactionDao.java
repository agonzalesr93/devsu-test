package com.agonzales.account.dao;

import com.agonzales.account.dao.repository.TransactionRepository;
import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.AccountStatus;
import com.agonzales.account.dto.domain.AccountType;
import com.agonzales.account.dto.domain.Transaction;
import com.agonzales.account.dto.domain.TransactionType;
import com.agonzales.account.dto.thridparty.db.AccountEntity;
import com.agonzales.account.dto.thridparty.db.TransactionEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TransactionDao {

    private TransactionRepository transactionRepository;

    public List<Transaction> getByAccountId(Integer accountId) {
        return transactionRepository.findByAccountIdOrderByIdDesc(accountId)
          .stream()
          .map(this::convertEntityToDomain)
          .collect(Collectors.toList());
    }

    public Optional<Transaction> getByAccountIdAndTransactionId(Integer accountId, Integer transactionId) {
        return transactionRepository.findByAccountIdAndId(accountId, transactionId)
          .map(this::convertEntityToDomain);
    }

    public Transaction create(Transaction transaction) {
        TransactionEntity transactionEntity = convertDomainToEntity(transaction);
        return convertEntityToDomain(transactionRepository.save(transactionEntity));
    }

    public Page<Transaction> getTransactionsBy(List<Integer> accountIds, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {
        LocalDateTime localDateTimeFrom = LocalDateTime.of(dateFrom, LocalTime.MIN);
        LocalDateTime localDateTimeTo = LocalDateTime.of(dateTo, LocalTime.MAX);

        Page<TransactionEntity> transactionEntityPage = transactionRepository.findByFilters(accountIds, localDateTimeFrom, localDateTimeTo, pageable);

        List<Transaction> transactions = transactionEntityPage.getContent()
          .stream()
          .map(this::convertEntityToDomain)
          .toList();

        Pageable pageableResponse = transactionEntityPage.getPageable();
        return new PageImpl<>(transactions, pageableResponse, transactionEntityPage.getTotalElements());
    }

    private Transaction convertEntityToDomain(TransactionEntity transactionEntity) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionEntity.getId());
        transaction.setAmount(transactionEntity.getAmount());
        transaction.setTransactionType(TransactionType.fromString(transactionEntity.getTransactionType()));
        transaction.setDate(transactionEntity.getDate());
        transaction.setBalance(transactionEntity.getBalance());

        Account account = new Account();
        account.setId(transactionEntity.getAccount().getId());
        account.setAccountNumber(transactionEntity.getAccount().getAccountNumber());
        account.setAccountType(AccountType.fromValue(transactionEntity.getAccount().getAccountType()));
        account.setInitialBalance(transactionEntity.getAccount().getInitialBalance());

        transaction.setAccount(account);

        return transaction;
    }

    private TransactionEntity convertDomainToEntity(Transaction transaction) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(transaction.getId());
        transactionEntity.setAmount(transaction.getAmount());
        transactionEntity.setTransactionType(transaction.getTransactionType().getType());
        transactionEntity.setDate(transaction.getDate());
        transactionEntity.setBalance(transaction.getBalance());

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(transaction.getAccount().getId());
        transactionEntity.setAccount(accountEntity);

        return transactionEntity;
    }

}
