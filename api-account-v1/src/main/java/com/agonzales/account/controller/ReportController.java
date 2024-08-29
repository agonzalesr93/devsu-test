package com.agonzales.account.controller;

import com.agonzales.account.dto.controller.report.AccountResponse;
import com.agonzales.account.dto.controller.report.AccountStatementLineResponse;
import com.agonzales.account.dto.controller.report.ClientResponse;
import com.agonzales.account.dto.controller.report.TransactionResponse;
import com.agonzales.account.dto.domain.AccountStatementLine;
import com.agonzales.account.service.ReportService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/reportes")
public class ReportController {

    private ReportService reportService;

    @GetMapping
    public Page<AccountStatementLineResponse> generateAccountStatement(
      @RequestParam("dateFrom") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateFrom,
      @RequestParam("dateTo") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateTo,
      @RequestParam("clientId") Integer clientId,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AccountStatementLine> accountStatementLinePage = reportService.generateAccountStatement(dateFrom, dateTo, clientId, pageable);

        List<AccountStatementLineResponse> accountStatementLinesResponse = accountStatementLinePage
          .getContent()
          .stream()
          .map(this::convertToResponse)
          .toList();

        Pageable pageableResponse = accountStatementLinePage.getPageable();
        return new PageImpl<>(accountStatementLinesResponse, pageableResponse, accountStatementLinePage.getTotalElements());
    }

    private AccountStatementLineResponse convertToResponse(AccountStatementLine accountStatementLine) {
        AccountStatementLineResponse response = new AccountStatementLineResponse();
        ClientResponse client = new ClientResponse();
        client.setId(accountStatementLine.getClient().getId());
        client.setName(accountStatementLine.getClient().getName());
        response.setClient(client);

        AccountResponse account = new AccountResponse();
        account.setId(accountStatementLine.getAccount().getId());
        account.setNumber(accountStatementLine.getAccount().getAccountNumber());
        account.setType(accountStatementLine.getAccount().getAccountType());

        response.setAccount(account);

        TransactionResponse transaction = new TransactionResponse();
        transaction.setId(accountStatementLine.getTransaction().getId());
        transaction.setAmount(accountStatementLine.getTransaction().getAmount());
        transaction.setDate(accountStatementLine.getTransaction().getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        transaction.setTransactionType(accountStatementLine.getTransaction().getTransactionType());
        transaction.setInitialBalance(accountStatementLine.getTransaction().getBalance().subtract(accountStatementLine.getTransaction().getAmount()));
        transaction.setCurrentBalance(accountStatementLine.getTransaction().getBalance());
        response.setTransaction(transaction);

        return response;
    }
}
