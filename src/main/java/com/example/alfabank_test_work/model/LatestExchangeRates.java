package com.example.alfabank_test_work.model;

import lombok.Data;

import java.util.Map;

@Data
public class LatestExchangeRates {

    private String disclaimer;
    private String license;
    private long timestamp;
    private String base;
    private Map<String, Double> rates;

}