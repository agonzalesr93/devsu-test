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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionDao transactionDao;

    @Mock
    private AccountDao accountDao;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByAccountId_returnsTransactionsForGivenAccountId() {
        Integer accountId = 1;
        Transaction transaction1 = createMockTransaction(BigDecimal.valueOf(100), TransactionType.DEPOSITO);
        Transaction transaction2 = createMockTransaction(BigDecimal.valueOf(-50), TransactionType.RETIRO);

        when(accountDao.getById(accountId)).thenReturn(Optional.of(createMockAccount()));
        when(transactionDao.getByAccountId(accountId)).thenReturn(Arrays.asList(transaction1, transaction2));

        List<Transaction> result = transactionService.getByAccountId(accountId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(TransactionType.DEPOSITO, result.get(0).getTransactionType());
        assertEquals(TransactionType.RETIRO, result.get(1).getTransactionType());
    }

    @Test
    void getByAccountId_throwsAccountNotFoundExceptionWhenAccountDoesNotExist() {
        Integer accountId = 1;

        when(accountDao.getById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> transactionService.getByAccountId(accountId));
    }

    @Test
    void getByAccountIdAndTransactionId_returnsTransactionForGivenIds() {
        Integer accountId = 1;
        Integer transactionId = 10;
        Transaction transaction = createMockTransaction(BigDecimal.valueOf(100), TransactionType.DEPOSITO);

        when(accountDao.getById(accountId)).thenReturn(Optional.of(createMockAccount()));
        when(transactionDao.getByAccountIdAndTransactionId(accountId, transactionId)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getByAccountIdAndTransactionId(accountId, transactionId);

        assertNotNull(result);
        assertEquals(TransactionType.DEPOSITO, result.getTransactionType());
    }

    @Test
    void getByAccountIdAndTransactionId_throwsTransactionNotFoundExceptionWhenTransactionDoesNotExist() {
        Integer accountId = 1;
        Integer transactionId = 10;

        when(accountDao.getById(accountId)).thenReturn(Optional.of(createMockAccount()));
        when(transactionDao.getByAccountIdAndTransactionId(accountId, transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getByAccountIdAndTransactionId(accountId, transactionId));
    }

    @Test
    void create_createsTransactionAndUpdatesAccountBalance() {
        Account account = createMockAccount();
        Transaction transaction = createMockTransaction(BigDecimal.valueOf(100), TransactionType.DEPOSITO);

        when(accountDao.getByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(transactionDao.create(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.create(transaction);

        assertNotNull(result);
        assertEquals(TransactionType.DEPOSITO, result.getTransactionType());
        assertEquals(BigDecimal.valueOf(200), result.getBalance());
        verify(accountDao).update(any(Account.class));
    }

    @Test
    void create_throwsInactiveAccountExceptionWhenAccountIsInactive() {
        Account account = createMockAccount();
        account.setStatus(AccountStatus.INACTIVO);
        Transaction transaction = createMockTransaction(BigDecimal.valueOf(100), null);

        when(accountDao.getByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertThrows(InactiveAccountException.class, () -> transactionService.create(transaction));
        verify(transactionDao, never()).create(any(Transaction.class));
    }

    @Test
    void create_throwsInsufficientBalanceExceptionWhenBalanceIsInsufficient() {
        Account account = createMockAccount();
        Transaction transaction = createMockTransaction(BigDecimal.valueOf(-200), null);

        when(accountDao.getByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertThrows(InsufficientBalanceException.class, () -> transactionService.create(transaction));
        verify(transactionDao, never()).create(any(Transaction.class));
    }

    private Account createMockAccount() {
        Account account = new Account();
        account.setId(1);
        account.setAccountNumber("1234567890");
        account.setInitialBalance(BigDecimal.valueOf(100));
        account.setStatus(AccountStatus.ACTIVO);
        return account;
    }

    private Transaction createMockTransaction(BigDecimal amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setAccount(createMockAccount());
        transaction.setDate(LocalDateTime.now());
        return transaction;
    }
}
