package com.agonzales.account.dto.controller.account;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCreateRequest {

    @NotNull(message = "Id cannot be null")
    Integer id;
}
