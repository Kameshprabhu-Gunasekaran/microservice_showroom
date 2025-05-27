package com.authservice.exception;

public class BadRequestServiceException extends RuntimeException {

    public BadRequestServiceException(String message) {
        super(message);
    }
}
