package com.agonzales.account.dto.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Transaction {

    private Integer         id;
    private LocalDateTime   date;
    private TransactionType transactionType;
    private BigDecimal      amount;
    private BigDecimal      balance;
    private Account         account;
}
