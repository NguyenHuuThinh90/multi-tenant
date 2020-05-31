package com.app.dto;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResponseErrorDto implements Serializable {

    private boolean state;
    private int errorCode;
    private String messages[];

    public ResponseErrorDto() { }

    public ResponseErrorDto(HttpStatus status, String[] msg, boolean state) {
        this.errorCode = status.value();
        this.messages = msg;
        this.state = state;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] message) {
        this.messages = message;
    }
}
