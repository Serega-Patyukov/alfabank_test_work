package com.example.alfabank_test_work.client;

import com.example.alfabank_test_work.model.LatestExchangeRates;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "${feign.client.name.one}", url = "${openexchangerates.url.general}")
public interface OpenExchangeRatesFeignClient {

    // Получаем курс переданной валюты на текущий момент.
    @GetMapping("/latest.json")
    LatestExchangeRates getLatest(@RequestParam String app_id,
                                  @RequestParam String symbols);
    // Получаем список доступных валют.
    @GetMapping("/currencies.json")
    Map<String, String> getCurrencies();

    // Получаем курс переданной валюты за указанную дату.
    @GetMapping("/historical/{date_json}.json")
    LatestExchangeRates getHistorical(@PathVariable String date_json,
                                      @RequestParam String app_id,
                                      @RequestParam String symbols);
}

