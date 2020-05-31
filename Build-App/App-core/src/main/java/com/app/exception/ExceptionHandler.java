package com.app.exception;


import com.app.dto.ResponseErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handle common exception
 *
 * @author thinhnguyen
 */
@ControllerAdvice
public class ExceptionHandler {

    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
    public ResponseEntity handleAppException(ApiException ex) {
        ResponseErrorDto error = new ResponseErrorDto(ex.getStatus(), ex.getMessages(), false);
        return new ResponseEntity(error, ex.getStatus());
    }
}
