package com.example.alfabank_test_work.controller;

import com.example.alfabank_test_work.servise.AlfaTestWorkServices;
import com.example.alfabank_test_work.client.OpenExchangeRatesFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Controller
@RequestMapping("/images")
@RequiredArgsConstructor
@Slf4j
public class AlfaTestWorkController {

    private final AlfaTestWorkServices alfaTestWorkServices;
    private final OpenExchangeRatesFeignClient openExchangeRatesFeignClient;

    @Value("${warn.message.notfound}")
    private String warnMessage;

    @GetMapping("/gif")
    public String getGif(@RequestParam String str) {

        String symbols = str.toUpperCase();

        Map<String, String> currencies = openExchangeRatesFeignClient.getCurrencies();

        if (!currencies.containsKey(symbols)) {
            log.warn(warnMessage);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        String url = alfaTestWorkServices.getGif(symbols);

        return "redirect:" + url;
    }
}