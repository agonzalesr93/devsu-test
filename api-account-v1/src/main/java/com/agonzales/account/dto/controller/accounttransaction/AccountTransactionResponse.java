package com.agonzales.account.dto.controller.accounttransaction;

import com.agonzales.account.dto.domain.TransactionType;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountTransactionResponse {

    private Integer         id;
    private String          date;
    private TransactionType type;
    private BigDecimal      amount;
    private BigDecimal      balance;
    private AccountResponse account;
}
