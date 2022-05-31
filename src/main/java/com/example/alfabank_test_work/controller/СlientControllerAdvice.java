package com.example.alfabank_test_work.controller;

import com.example.alfabank_test_work.exceptions.ExceptionsNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class СlientControllerAdvice {
    @ResponseBody
    @ExceptionHandler(ExceptionsNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(ExceptionsNotFound exceptionsNotFound) {
        return exceptionsNotFound.getMessage();
    }
}
