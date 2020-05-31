package com.app.common;

public enum OrderQuery {
    ASC("ASC"),
    DESC("DESC");

    private String name;
    OrderQuery(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
