package com.agonzales.account.dto.controller.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountStatementLineResponse {

    private ClientResponse      client;
    private AccountResponse     account;
    private TransactionResponse transaction;
}
