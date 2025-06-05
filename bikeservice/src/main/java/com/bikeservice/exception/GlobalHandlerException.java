package com.bikeservice.exception;

import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(BadRequestServiceException.class)
    public ResponseDTO handleBadRequestAlertException(final BadRequestServiceException exception) {
        exception.printStackTrace();
        return new ResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                Constant.NOT_FOUND,
                null
        );
    }
}
