package com.agonzales.account.controller;

import com.agonzales.account.dto.controller.account.AccountCreateRequest;
import com.agonzales.account.dto.controller.account.AccountCreateResponse;
import com.agonzales.account.dto.controller.account.AccountResponse;
import com.agonzales.account.dto.controller.account.ClientResponse;
import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.Client;
import com.agonzales.account.service.AccountService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/cuentas")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountResponse> getAll() {
        return accountService.getAll().stream()
          .map(this::convertDomainToResponse)
          .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AccountResponse getById(@PathVariable Integer id) {
        return convertDomainToResponse(accountService.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountCreateResponse create(@Valid @RequestBody AccountCreateRequest accountCreateRequest) {
        return convertDomainToCreateResponse(accountService.create(convertRequestToDomain(accountCreateRequest)));
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable Integer id) {
        throw new UnsupportedOperationException("Account information cannot be updated.");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        accountService.delete(id);
    }

    private Account convertRequestToDomain(AccountCreateRequest accountCreateRequest) {
        Account account = new Account();
        account.setAccountNumber(accountCreateRequest.getAccountNumber());
        account.setAccountType(accountCreateRequest.getAccountType());
        account.setInitialBalance(accountCreateRequest.getInitialBalance());
        account.setStatus(accountCreateRequest.getStatus());
        Client client = new Client();
        client.setId(accountCreateRequest.getClient().getId());
        account.setClient(client);
        return account;
    }

    private AccountResponse convertDomainToResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setAccountType(account.getAccountType());
        accountResponse.setInitialBalance(account.getInitialBalance());
        accountResponse.setStatus(account.getStatus());

        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(account.getClient().getId());
        clientResponse.setName(account.getClient().getName());
        accountResponse.setClient(clientResponse);

        return accountResponse;
    }

    private AccountCreateResponse convertDomainToCreateResponse(Account account) {
        AccountCreateResponse accountCreateResponse = new AccountCreateResponse();
        accountCreateResponse.setId(account.getId());
        return accountCreateResponse;
    }
}
