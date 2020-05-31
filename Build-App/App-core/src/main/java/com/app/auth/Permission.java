package com.app.auth;

public enum Permission {
    CREATE("NEW"),
    UPDATE("EDIT");

    private String value;

    Permission(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
