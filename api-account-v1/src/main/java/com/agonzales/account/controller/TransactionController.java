package com.agonzales.account.controller;

import com.agonzales.account.dto.controller.transaction.TransactionCreateRequest;
import com.agonzales.account.dto.controller.transaction.TransactionCreateResponse;
import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.Transaction;
import com.agonzales.account.service.TransactionService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/movimientos")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionCreateResponse create(@Valid @RequestBody TransactionCreateRequest transactionCreateRequest) {
        return convertDomainToCreateResponse(transactionService.create(convertRequestToDomain(transactionCreateRequest)));
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable Integer id) {
        throw new UnsupportedOperationException("Transaction information cannot be updated.");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        throw new UnsupportedOperationException("Transaction information cannot be deleted.");
    }

    private Transaction convertRequestToDomain(TransactionCreateRequest transactionCreateRequest) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionCreateRequest.getAmount());

        Account account = new Account();
        account.setAccountNumber(transactionCreateRequest.getAccount().getAccountNumber());
        transaction.setAccount(account);

        return transaction;
    }

    private TransactionCreateResponse convertDomainToCreateResponse(Transaction transaction) {
        TransactionCreateResponse transactionCreateResponse = new TransactionCreateResponse();
        transactionCreateResponse.setId(transaction.getId());
        return transactionCreateResponse;
    }
}
