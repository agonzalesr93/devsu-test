package com.agonzales.client.dto.domain;

public enum ClientStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked");

    private String status;

    ClientStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ClientStatus fromString(String status) {
        for (ClientStatus s : ClientStatus.values()) {
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
