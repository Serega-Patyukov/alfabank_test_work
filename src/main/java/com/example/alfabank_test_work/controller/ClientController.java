package com.example.alfabank_test_work.controller;

import com.example.alfabank_test_work.exceptions.ExceptionsNotFound;
import com.example.alfabank_test_work.model.Latest;
import com.example.alfabank_test_work.servise.ClientServices;
import com.example.alfabank_test_work.utils.JsonFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientServices clientServices;
    private final JsonFeignClient jsonFeignClient;

    @GetMapping("/{symbols}")
    public Latest getLatest(@PathVariable String str) {

        String symbols = str.toUpperCase();

        Map<String, String> currencies = jsonFeignClient.getCurrencies();

        if (!currencies.containsKey(symbols)) {
            log.warn("Код валюты не найден");
            throw new ExceptionsNotFound();
        }

        return clientServices.getLatest(symbols);
    }
}
