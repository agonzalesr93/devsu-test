package com.agonzales.client.dto.domain;

public enum Gender {

    MALE("Male"),
    FEMALE("Female");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Gender fromString(String status) {
        for (Gender s : Gender.values()) {
            if (s.value.equalsIgnoreCase(status)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
