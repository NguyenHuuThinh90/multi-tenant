package com.app.web.dto;

import java.io.Serializable;
import java.util.List;

public class DataRequest implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
