package com.agonzales.account.dao;

import com.agonzales.account.dao.repository.AccountRepository;
import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.AccountType;
import com.agonzales.account.dto.domain.Client;
import com.agonzales.account.dto.domain.AccountStatus;
import com.agonzales.account.dto.thridparty.db.AccountEntity;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AccountDao {

    private AccountRepository accountRepository;

    public List<Account> getAll() {
        return accountRepository.findAll().stream()
          .map(this::convertEntityToDomain)
          .collect(Collectors.toList());
    }

    public List<Account> getAllByClientId(Integer clientId) {
        return accountRepository.findAllByClientId(clientId).stream()
          .map(this::convertEntityToDomain)
          .collect(Collectors.toList());
    }

    public Optional<Account> getById(Integer id) {
        return accountRepository.findById(id)
          .map(this::convertEntityToDomain);
    }

    public Optional<Account> getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
          .map(this::convertEntityToDomain);
    }

    public Account create(Account account) {
        AccountEntity accountEntity = convertDomainToEntity(account);
        return convertEntityToDomain(accountRepository.save(accountEntity));
    }

    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    public Account update(Account account) {
        AccountEntity accountEntity = convertDomainToEntity(account);
        return convertEntityToDomain(accountRepository.save(accountEntity));
    }

    public void delete(Integer id) {
        accountRepository.updateStatusById(id, AccountStatus.INACTIVO.getStatus());
    }

    private Account convertEntityToDomain(AccountEntity accountEntity) {
        Account account = new Account();
        account.setId(accountEntity.getId());
        account.setAccountNumber(accountEntity.getAccountNumber());
        account.setAccountType(AccountType.fromValue(accountEntity.getAccountType()));
        account.setInitialBalance(accountEntity.getInitialBalance());
        account.setStatus(AccountStatus.fromString(accountEntity.getStatus()));
        Client client = new Client();
        client.setId(accountEntity.getClientId());
        account.setClient(client);
        return account;
    }

    private AccountEntity convertDomainToEntity(Account account) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(account.getId());
        accountEntity.setAccountNumber(account.getAccountNumber());
        accountEntity.setAccountType(account.getAccountType().getValue());
        accountEntity.setInitialBalance(account.getInitialBalance());
        accountEntity.setStatus(account.getStatus().getStatus());
        accountEntity.setClientId(account.getClient().getId());
        return accountEntity;
    }
}
