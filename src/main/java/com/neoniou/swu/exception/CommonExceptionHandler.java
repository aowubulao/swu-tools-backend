package com.neoniou.swu.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author neo.zzj
 */
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(NeoException.class)
    public ResponseEntity<ExceptionResult> handleException(NeoException e) {
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        return ResponseEntity.status(exceptionEnum.getStatusCode()).body(new ExceptionResult(e.getExceptionEnum()));
    }
}

