package com.agonzales.account.dto.domain;

public enum TransactionType {

    RETIRO("RETIRO"),
    DEPOSITO("DEPOSITO");

    private String type;

    TransactionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static TransactionType fromString(String type) {
        for (TransactionType t : TransactionType.values()) {
            if (t.type.equalsIgnoreCase(type)) {
                return t;
            }
        }
        return null;
    }
}
