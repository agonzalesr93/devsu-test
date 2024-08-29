package com.agonzales.account.dto.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Account {

    private Integer       id;
    private String        accountNumber;
    private AccountType   accountType;
    private BigDecimal    initialBalance;
    private AccountStatus status;
    private Client        client;
}
