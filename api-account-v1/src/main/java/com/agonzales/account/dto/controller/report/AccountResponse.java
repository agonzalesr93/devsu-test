package com.agonzales.account.dto.controller.report;

import com.agonzales.account.dto.domain.AccountType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse {

    private Integer     id;
    private String      number;
    private AccountType type;
}
