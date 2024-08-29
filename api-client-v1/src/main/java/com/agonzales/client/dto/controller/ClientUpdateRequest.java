package com.agonzales.client.dto.controller;

import com.agonzales.client.dto.domain.ClientStatus;
import com.agonzales.client.dto.domain.Gender;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientUpdateRequest {

    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String       password;
    private ClientStatus status;

    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String name;
    private Gender gender;

    @Min(value = 0, message = "Age must be great equal to zero")
    @Max(value = 100, message = "Age must be less than 100")
    private Integer age;
    private String  address;
    private String  phone;
}
