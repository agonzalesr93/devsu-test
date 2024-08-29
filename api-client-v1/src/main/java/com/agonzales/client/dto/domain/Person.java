package com.agonzales.client.dto.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {

    private Integer personId;
    private String name;
    private Gender gender;
    private Integer age;
    private String address;
    private String phone;
}
