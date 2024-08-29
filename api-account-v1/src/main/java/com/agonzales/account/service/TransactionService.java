package com.agonzales.account.service;

import com.agonzales.account.dao.AccountDao;
import com.agonzales.account.dao.TransactionDao;
import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.AccountStatus;
import com.agonzales.account.dto.domain.Transaction;
import com.agonzales.account.dto.domain.TransactionType;
import com.agonzales.account.exception.AccountNotFoundException;
import com.agonzales.account.exception.InactiveAccountException;
import com.agonzales.account.exception.InsufficientBalanceException;
import com.agonzales.account.exception.TransactionNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionDao transactionDao;
    private AccountDao     accountDao;

    public List<Transaction> getByAccountId(Integer accountId) {

        getAccountById(accountId);

        return transactionDao.getByAccountId(accountId);
    }

    public Transaction getByAccountIdAndTransactionId(Integer accountId, Integer transactionId) {

        getAccountById(accountId);

        return transactionDao.getByAccountIdAndTransactionId(accountId, transactionId)
          .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public Transaction create(Transaction transaction) {

        Account account = accountDao.getByAccountNumber(transaction.getAccount().getAccountNumber())
          .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        transaction.setAccount(account);

        if (AccountStatus.ACTIVO != account.getStatus()) {
            throw new InactiveAccountException("Account must be active");
        }

        BigDecimal newBalance = account.getInitialBalance().add(transaction.getAmount());

        if (BigDecimal.ZERO.compareTo(newBalance) > 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            transaction.setTransactionType(TransactionType.RETIRO);
        } else {
            transaction.setTransactionType(TransactionType.DEPOSITO);
        }

        transaction.setDate(LocalDateTime.now());
        transaction.setBalance(newBalance);

        Transaction createdTransaction = transactionDao.create(transaction);

        account.setInitialBalance(newBalance);
        accountDao.update(account);

        return createdTransaction;
    }

    private Account getAccountById(Integer accountId) {
        return accountDao.getById(accountId)
          .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }
}

