package com.authservice.exception;

import com.authservice.dto.ResponseDTO;
import com.authservice.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(BadRequestServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handleBadRequestAlertException(BadRequestServiceException exception) {
        ResponseDTO responseDTO = new ResponseDTO();
        exception.printStackTrace();
        responseDTO.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
        responseDTO.setMessage(Constant.NOT_FOUND);
        responseDTO.setData(null);
        return responseDTO;
    }
}
