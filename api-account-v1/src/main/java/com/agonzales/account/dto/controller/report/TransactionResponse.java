package com.agonzales.account.dto.controller.report;

import com.agonzales.account.dto.domain.TransactionType;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionResponse {

    private Integer         id;
    private String          date;
    private BigDecimal      initialBalance;
    private BigDecimal      currentBalance;
    private TransactionType transactionType;
    private BigDecimal      amount;
}
