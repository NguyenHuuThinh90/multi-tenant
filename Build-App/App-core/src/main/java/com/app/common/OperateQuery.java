package com.app.common;

public enum OperateQuery {

    GREATE_THAN("GREATE_THAN"),
    GREATE_THAN_OR_EQUAL("GREATE_THAN_OR_EQUAL"),
    LESS_THAN("LESS_THAN"),
    LESS_THAN_OR_EQUAL("LESS_THAN_OR_EQUAL"),
    EQUAL("EQUAL"),
    NOT_EQUAL("NOT_EQUAL");

    private String name;

    OperateQuery(String name) {
        this.name = name;
    }

    /**
     * Get name
     * <p>
     * return value of name
     */
    public String getName() {
        return name;
    }
}
