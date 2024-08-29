package com.agonzales.account.dto.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountStatementLine {

    private Client      client;
    private Account     account;
    private Transaction transaction;
}
