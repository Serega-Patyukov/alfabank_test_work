package com.example.alfabank_test_work.controller;

import com.example.alfabank_test_work.exceptions.AlfaTestWorExceptions;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AlfaTestWorkControllerAdvice {
    @ResponseBody
    @ExceptionHandler(AlfaTestWorExceptions.class)
    String getMessageExceptions(AlfaTestWorExceptions alfaTestWorExceptions) {
        return alfaTestWorExceptions.getMessage();
    }
}
