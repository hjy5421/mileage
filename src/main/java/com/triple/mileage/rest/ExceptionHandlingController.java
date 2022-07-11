package com.triple.mileage.rest;


import com.triple.mileage.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingController {
    private final Logger log = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result illegalArgumentHandle(IllegalArgumentException e) {
        log.error("[EXCEPTION] {}", e.getMessage());
        return Result.clientError(e.getMessage());
    }


}
