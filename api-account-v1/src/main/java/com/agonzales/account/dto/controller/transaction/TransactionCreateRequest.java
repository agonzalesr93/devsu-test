package com.agonzales.account.dto.controller.transaction;

import com.agonzales.account.validator.NotZero;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionCreateRequest {

    @NotNull(message = "Amount cannot be null")
    @NotZero(message = "Amount must not be zero")
    private BigDecimal amount;

    @Valid
    @NotNull(message = "Account cannot be null")
    private AccountCreateRequest account;
}
