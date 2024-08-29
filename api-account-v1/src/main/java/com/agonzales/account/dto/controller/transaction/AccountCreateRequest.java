package com.agonzales.account.dto.controller.transaction;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreateRequest {

    @NotBlank(message = "Account number is required")
    private String accountNumber;
}
