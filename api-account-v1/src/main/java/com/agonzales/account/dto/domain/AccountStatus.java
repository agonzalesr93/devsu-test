package com.agonzales.account.dto.domain;

public enum AccountStatus {

    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO");

    private String status;

    AccountStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static AccountStatus fromString(String status) {
        for (AccountStatus s : AccountStatus.values()) {
            if (s.status.equalsIgnoreCase(status)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return status;
    }
}
