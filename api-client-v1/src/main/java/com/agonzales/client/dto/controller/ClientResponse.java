package com.agonzales.client.dto.controller;

import com.agonzales.client.dto.domain.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponse {

    private Integer id;
    private String  password;
    private String  status;
    private Integer personId;
    private String  name;
    private Gender  gender;
    private Integer age;
    private String  address;
    private String  phone;
}
