package com.app.dto;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResponseDto implements Serializable {

    private boolean state;
    private int code;
    private Object result;

    public ResponseDto() {
    }

    public ResponseDto(HttpStatus status, Object result, boolean state) {
        this.code = status.value();
        this.result = result;
        this.state = state;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
