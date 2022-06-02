package com.example.alfabank_test_work.servise;

import com.example.alfabank_test_work.model.LatestExchangeRates;
import com.example.alfabank_test_work.client.GiphyFeignClient;
import com.example.alfabank_test_work.client.OpenExchangeRatesFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlfaTestWorkServices {

    private final OpenExchangeRatesFeignClient openExchangeRatesFeignClient;
    private final GiphyFeignClient giphyFeignClient;

    @Value("${openexchangerates.app.id}")
    private String app_id;
    @Value("${giphy.api.key}")
    private String api_key;
    @Value("${giphy.broke}")
    private String broke;
    @Value("${giphy.rich}")
    private String rich;

    public Object getLatest(String symbols) {

        LocalDate dateYesterday = LocalDate.now().minusDays(1L);

        LatestExchangeRates today = openExchangeRatesFeignClient.getLatest(app_id, symbols);
        LatestExchangeRates yesterday = openExchangeRatesFeignClient.getHistorical(dateYesterday.toString(), app_id, symbols);

        if ( today.getRates().get(symbols) > yesterday.getRates().get(symbols) ) {
            log.info("Курс " + symbols + " по отношению к USD за сегодня стал ниже вчерашнего (tag = broke)");
            return giphyFeignClient.getRandomGif(api_key, broke);
        }
        else {
            log.info("Курс " + symbols + " по отношению к USD за сегодня стал выше вчерашнего (tag = rich)");
            return giphyFeignClient.getRandomGif(api_key, rich);
        }
    }
}
