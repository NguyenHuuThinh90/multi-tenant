package com.app.web.dto;

import java.io.Serializable;
import java.util.List;

public class UserRequestDto implements Serializable {

    private String name;
    private List<DataRequest> datas;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataRequest> getDatas() {
        return datas;
    }

    public void setDatas(List<DataRequest> datas) {
        this.datas = datas;
    }
}
