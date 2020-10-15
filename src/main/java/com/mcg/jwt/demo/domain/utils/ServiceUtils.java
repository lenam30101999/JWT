package com.mcg.jwt.demo.domain.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtils {
    @Autowired
    public ServiceUtils() {
    }

    public <T> ResponseEntity<T> createOkResponse(T body) {
        return createResponse(body, HttpStatus.OK);
    }

    public <T> ResponseEntity<T> createResponse(T body, HttpStatus httpStatus) {
        return new ResponseEntity<>(body, httpStatus);
    }

    public <T> ResponseEntity<T> createErrorResponse(T body) {
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
