package com.agonzales.client.config.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfoMessage {
    private UUID    id;
    private Integer clientId;
    private String name;
    private boolean error;
    private String errorDetail;
}
