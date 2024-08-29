package com.agonzales.account.dto.domain;

public enum AccountType {

    AHORROS("AHORROS"),
    CORRIENTE("CORRIENTE");

    private String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AccountType fromValue(String value) {
        for (AccountType accountType : AccountType.values()) {
            if (accountType.getValue().equals(value)) {
                return accountType;
            }
        }
        return null;
    }
}
