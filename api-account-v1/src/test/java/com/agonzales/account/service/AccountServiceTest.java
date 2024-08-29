package com.agonzales.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agonzales.account.dao.AccountDao;
import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.Client;
import com.agonzales.account.exception.AccountNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class AccountServiceTest {

    @Mock
    private AccountDao accountDao;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_returnsListOfAccounts() {
        Account account1 = createMockAccount(1, "123456", new BigDecimal("1000.00"));
        Account account2 = createMockAccount(2, "789012", new BigDecimal("2000.00"));

        when(accountDao.getAll()).thenReturn(Arrays.asList(account1, account2));

        List<Account> accounts = accountService.getAll();

        assertEquals(2, accounts.size());
        assertEquals("123456", accounts.get(0).getAccountNumber());
        assertEquals("789012", accounts.get(1).getAccountNumber());
    }

    @Test
    void getById_returnsAccount_whenAccountExists() {
        Account account = createMockAccount(1, "123456", new BigDecimal("1000.00"));

        when(accountDao.getById(1)).thenReturn(Optional.of(account));

        Account result = accountService.getById(1);

        assertNotNull(result);
        assertEquals("123456", result.getAccountNumber());
    }

    @Test
    void getById_throwsAccountNotFoundException_whenAccountDoesNotExist() {
        when(accountDao.getById(1)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getById(1));
    }

    @Test
    void create_savesAndReturnsAccount() {
        Account account = createMockAccount(1, "123456", new BigDecimal("1000.00"));

        when(accountDao.create(any(Account.class))).thenReturn(account);

        Account result = accountService.create(account);

        assertNotNull(result);
        assertEquals("123456", result.getAccountNumber());
        verify(accountDao).create(any(Account.class));
    }

    @Test
    void update_updatesAndReturnsAccount() {
        Account existingAccount = createMockAccount(1, "123456", new BigDecimal("1000.00"));
        Account updatedAccount = createMockAccount(1, "654321", new BigDecimal("2000.00"));

        when(accountDao.getById(1)).thenReturn(Optional.of(existingAccount));
        when(accountDao.update(any(Account.class))).thenReturn(updatedAccount);

        Account result = accountService.update(updatedAccount);

        assertNotNull(result);
        assertEquals("654321", result.getAccountNumber());
        assertEquals(new BigDecimal("2000.00"), result.getInitialBalance());
        verify(accountDao).update(any(Account.class));
    }

    @Test
    void delete_deletesAccount_whenAccountExists() {
        Account account = createMockAccount(1, "123456", new BigDecimal("1000.00"));

        when(accountDao.getById(1)).thenReturn(Optional.of(account));
        doNothing().when(accountDao).delete(1);

        accountService.delete(1);

        verify(accountDao).delete(1);
    }

    @Test
    void delete_throwsAccountNotFoundException_whenAccountDoesNotExist() {
        when(accountDao.getById(1)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.delete(1));
    }

    private Account createMockAccount(Integer id, String accountNumber, BigDecimal initialBalance) {
        Account account = new Account();
        account.setId(id);
        account.setAccountNumber(accountNumber);
        account.setInitialBalance(initialBalance);
        account.setClient(new Client());
        return account;
    }
}
