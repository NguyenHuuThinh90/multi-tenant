package com.app.exception;

import org.springframework.http.HttpStatus;

/**
 * Api exception
 * Handle common excetion
 */
public class ApiException extends RuntimeException {

    private HttpStatus status;
    private String[] messages;

    public ApiException(HttpStatus status, String... msg) {
        this.messages = msg;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessage(String[] message) {
        this.messages = message;
    }
}
