package com.agonzales.account.dto.controller.account;

import com.agonzales.account.dto.domain.AccountStatus;
import com.agonzales.account.dto.domain.AccountType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountCreateRequest {

    @NotBlank(message = "Account number cannot be blank")
    @Size(max = 50, message = "Account number cannot be longer than 50 characters")
    private String accountNumber;

    @NotNull(message = "Account type cannot be null")
    private AccountType accountType;

    @NotNull(message = "Initial balance cannot be null")
    @Min(value = 0, message = "Initial balance must be great equal to zero")
    private BigDecimal initialBalance;

    @NotNull(message = "Status cannot be null")
    private AccountStatus status;

    @Valid
    @NotNull(message = "Client cannot be null")
    private ClientCreateRequest client;

}
