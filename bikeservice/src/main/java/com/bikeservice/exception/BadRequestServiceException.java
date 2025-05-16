package com.bikeservice.exception;

public class BadRequestServiceException extends RuntimeException {

    public BadRequestServiceException(String message) {
        super(message);
    }
}
