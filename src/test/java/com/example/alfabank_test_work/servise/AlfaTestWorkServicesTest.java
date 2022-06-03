package com.example.alfabank_test_work.servise;

import com.example.alfabank_test_work.client.GiphyFeignClient;
import com.example.alfabank_test_work.client.OpenExchangeRatesFeignClient;
import com.example.alfabank_test_work.model.LatestExchangeRates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AlfaTestWorkServicesTest {

    @Autowired
    private GiphyFeignClient giphyFeignClient;
    @Autowired
    private OpenExchangeRatesFeignClient openExchangeRatesFeignClient;
    @Autowired
    private AlfaTestWorkServices alfaTestWorkServices;

    @Value("${openexchangerates.app.id}")
    private String app_id;

    @Test
    void getRandomGifTest() {

        /*
        data1 - курс RUB по отношению к USD за 2022-03-25.
        data2 - курс RUB по отношению к USD за 2022-04-25.
        Между выбранными датами курс RUB растет.
         */
        LatestExchangeRates data1 = openExchangeRatesFeignClient.getHistorical("2022-03-25", app_id, "RUB");
        LatestExchangeRates data2 = openExchangeRatesFeignClient.getHistorical("2022-04-25", app_id, "RUB");
        Assertions.assertTrue(alfaTestWorkServices.getRandomGif(data2, data1, "RUB") instanceof String);

        /*
        data1 - курс RUB по отношению к USD за 2021-12-30.
        data2 - курс RUB по отношению к USD за 2022-03-10.
        Между выбранными датами курс RUB падает.
         */
        LatestExchangeRates data3 = openExchangeRatesFeignClient.getHistorical("2021-12-30", app_id, "RUB");
        LatestExchangeRates data4 = openExchangeRatesFeignClient.getHistorical("2022-03-10", app_id, "RUB");
        Assertions.assertTrue(alfaTestWorkServices.getRandomGif(data4, data3, "RUB") instanceof String);
    }
}
