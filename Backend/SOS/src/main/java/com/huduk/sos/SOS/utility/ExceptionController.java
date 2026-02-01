package com.huduk.sos.SOS.utility;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.huduk.sos.SOS.dto.ErrorInfo;
import com.huduk.sos.SOS.exception.SOSException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorInfo> handleIOException(IOException exception) {
        ErrorInfo error = new ErrorInfo("SERVICE.IO_EXCEPTION");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SOSException.class)
    public ResponseEntity<ErrorInfo> handleSOSException(SOSException exception) {
        ErrorInfo error = new ErrorInfo(exception.getMessage());
        return new ResponseEntity<>(error, exception.getStatus());
    }
}
