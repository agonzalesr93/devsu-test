package com.agonzales.client.dto.controller;

import com.agonzales.client.dto.domain.ClientStatus;
import com.agonzales.client.dto.domain.Gender;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCreateRequest {

    @NotBlank(message = "Password cannot be blank")
    @Size(max = 50, message = "Password cannot be longer than 50 characters")
    private String password;

    @NotNull(message = "Status cannot be null")
    private ClientStatus status;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String name;

    @NotNull
    private Gender gender;

    @NotNull
    @Min(value = 0, message = "Age must be great equal to zero")
    @Max(value = 100, message = "Age must be less than 100")
    private Integer age;

    @NotEmpty(message = "Address cannot be empty")
    private String address;

    @NotEmpty(message = "Phone cannot be empty")
    private String phone;
}
