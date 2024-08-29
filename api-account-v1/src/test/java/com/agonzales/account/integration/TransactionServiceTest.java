package com.agonzales.account.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.AccountStatus;
import com.agonzales.account.dto.domain.AccountType;
import com.agonzales.account.dto.domain.Client;
import com.agonzales.account.dto.domain.Transaction;
import com.agonzales.account.dto.domain.TransactionType;
import com.agonzales.account.exception.AccountNotFoundException;
import com.agonzales.account.exception.InactiveAccountException;
import com.agonzales.account.exception.InsufficientBalanceException;
import com.agonzales.account.service.AccountService;
import com.agonzales.account.service.TransactionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @Test
    void throw_AccountNoFoundException_When_CreateTransaction() {

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(500));

        Account transactionAccount = new Account();
        transactionAccount.setAccountNumber("12333344");
        transaction.setAccount(transactionAccount);

        AccountNotFoundException thrown = assertThrows(AccountNotFoundException.class, () -> {
            transactionService.create(transaction);
        });

        assertEquals("Account not found", thrown.getMessage());
    }

    @Test
    void throw_InactiveAccountException_When_CreateTransaction() {

        Account account = new Account();
        account.setId(1);
        account.setAccountNumber("7778888");
        account.setAccountType(AccountType.AHORROS);
        account.setInitialBalance(BigDecimal.valueOf(500));
        account.setStatus(AccountStatus.INACTIVO);

        Client client = new Client();
        client.setId(55);

        account.setClient(client);

        accountService.create(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(500));

        Account transactionAccount = new Account();
        transactionAccount.setAccountNumber("7778888");
        transaction.setAccount(transactionAccount);

        InactiveAccountException thrown = assertThrows(InactiveAccountException.class, () -> {
            transactionService.create(transaction);
        });

        assertEquals("Account must be active", thrown.getMessage());
    }

    @Test
    void throw_InsufficientBalanceException_When_CreateTransaction() {

        Account account = new Account();
        account.setId(1);
        account.setAccountNumber("22255566");
        account.setAccountType(AccountType.AHORROS);
        account.setInitialBalance(BigDecimal.valueOf(500));
        account.setStatus(AccountStatus.ACTIVO);

        Client client = new Client();
        client.setId(55);

        account.setClient(client);

        accountService.create(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(-550));

        Account transactionAccount = new Account();
        transactionAccount.setAccountNumber("22255566");
        transaction.setAccount(transactionAccount);

        InsufficientBalanceException thrown = assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.create(transaction);
        });

        assertEquals("Saldo no disponible", thrown.getMessage());
    }

    @Test
    void when_createTransaction_Then_TransactionTypeIsRetiro() {

        Account account = new Account();
        account.setId(1);
        account.setAccountNumber("123333");
        account.setAccountType(AccountType.AHORROS);
        account.setInitialBalance(BigDecimal.valueOf(500));
        account.setStatus(AccountStatus.ACTIVO);

        Client client = new Client();
        client.setId(55);

        account.setClient(client);

        accountService.create(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(-100));

        Account transactionAccount = new Account();
        transactionAccount.setAccountNumber("123333");
        transaction.setAccount(transactionAccount);

        Transaction createdTransaction = transactionService.create(transaction);

        assertNotNull(createdTransaction.getId());
        assertEquals(createdTransaction.getTransactionType(), TransactionType.RETIRO);
    }

    @Test
    void when_createTransaction_Then_TransactionTypeIsDeposito() {

        Account account = new Account();
        account.setId(1);
        account.setAccountNumber("123333");
        account.setAccountType(AccountType.AHORROS);
        account.setInitialBalance(BigDecimal.valueOf(500));
        account.setStatus(AccountStatus.ACTIVO);

        Client client = new Client();
        client.setId(55);

        account.setClient(client);

        accountService.create(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));

        Account transactionAccount = new Account();
        transactionAccount.setAccountNumber("123333");
        transaction.setAccount(transactionAccount);

        Transaction createdTransaction = transactionService.create(transaction);

        assertNotNull(createdTransaction.getId());
        assertEquals(createdTransaction.getTransactionType(), TransactionType.DEPOSITO);
    }
}
