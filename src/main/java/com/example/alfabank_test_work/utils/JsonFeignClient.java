package com.example.alfabank_test_work.utils;

import com.example.alfabank_test_work.model.Latest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value = "Serega", url = "https://openexchangerates.org/api/")
public interface JsonFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/latest.json?app_id={app_id}&symbols={symbols}")
    Latest getLatest(@PathVariable String app_id, @PathVariable String symbols);

    @RequestMapping(method = RequestMethod.GET, value = "/currencies.json")
    Map<String, String> getCurrencies();

    @RequestMapping(method = RequestMethod.GET, value = "/historical/{date_json}?app_id={app_id}&symbols={symbols}")
    Latest getHistorical(@PathVariable String date_json, @PathVariable String app_id, @PathVariable String symbols);
}

