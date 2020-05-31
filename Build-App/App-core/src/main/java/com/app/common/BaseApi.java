package com.app.common;

import com.app.dto.ResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseApi {

    public ResponseEntity<ResponseDto> buildResponse(Object data, HttpStatus status) {
        ResponseDto result = new ResponseDto(status, data, true);
        return new ResponseEntity(result, status);
    }

    public ResponseEntity<ResponseDto> buildResponse(Object data, HttpHeaders headers, HttpStatus status) {
        ResponseDto result = new ResponseDto(status, data, true);
        return new ResponseEntity(result, headers, status);
    }
}
