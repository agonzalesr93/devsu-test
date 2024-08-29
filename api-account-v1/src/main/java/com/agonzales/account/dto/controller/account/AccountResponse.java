package com.agonzales.account.dto.controller.account;

import com.agonzales.account.dto.domain.AccountStatus;
import com.agonzales.account.dto.domain.AccountType;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountResponse {

    private Integer        id;
    private String         accountNumber;
    private AccountType    accountType;
    private BigDecimal     initialBalance;
    private AccountStatus  status;
    private ClientResponse client;
}
