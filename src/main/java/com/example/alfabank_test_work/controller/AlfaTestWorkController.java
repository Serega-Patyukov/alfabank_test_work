package com.example.alfabank_test_work.controller;

import com.example.alfabank_test_work.servise.AlfaTestWorkServices;
import com.example.alfabank_test_work.utils.OpenExchangeRatesFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AlfaTestWorkController {

    private final AlfaTestWorkServices alfaTestWorkServices;
    private final OpenExchangeRatesFeignClient openExchangeRatesFeignClient;

    @GetMapping("/{str}")
    public Object getLatest(@PathVariable String str) {

        String symbols = str.toUpperCase();

        Map<String, String> currencies = openExchangeRatesFeignClient.getCurrencies();

        if (!currencies.containsKey(symbols)) {
            log.warn("Код валюты не найден");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return alfaTestWorkServices.getLatest(symbols);
    }
}
