package com.agonzales.account.service;

import com.agonzales.account.dao.AccountDao;
import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.Client;
import com.agonzales.account.exception.AccountNotFoundException;
import com.agonzales.account.exception.AccountNumberAlreadyExistsException;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountDao accountDao;

    public List<Account> getAll() {
        return accountDao.getAll();
    }

    public Account getById(Integer id) {
        return accountDao.getById(id)
              .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    public Account create(Account account) {
        if (accountDao.existsByAccountNumber(account.getAccountNumber())) {
            throw new AccountNumberAlreadyExistsException("Account number already exists");
        }

        return accountDao.create(account);
    }

    public Account update(Account account) {

        Account currentAccount = getById(account.getId());

        currentAccount.setAccountNumber(account.getAccountNumber() != null ? account.getAccountNumber() : currentAccount.getAccountNumber());
        currentAccount.setAccountType(account.getAccountType() != null ? account.getAccountType() : currentAccount.getAccountType());
        currentAccount.setInitialBalance(account.getInitialBalance() != null ? account.getInitialBalance() : currentAccount.getInitialBalance());
        currentAccount.setStatus(account.getStatus() != null ? account.getStatus() : currentAccount.getStatus());
        currentAccount.setClient(account.getClient() != null ? account.getClient() : currentAccount.getClient());

        return accountDao.update(currentAccount);
    }

    public void delete(Integer id) {
        getById(id);
        accountDao.delete(id);
    }
}

