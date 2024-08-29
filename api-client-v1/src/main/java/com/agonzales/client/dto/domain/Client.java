package com.agonzales.client.dto.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Client extends Person {

    private Integer id;
    private String password;
    private ClientStatus status;

}
