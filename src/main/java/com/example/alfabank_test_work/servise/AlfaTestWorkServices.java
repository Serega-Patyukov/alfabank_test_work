package com.example.alfabank_test_work.servise;

import com.example.alfabank_test_work.model.GifData;
import com.example.alfabank_test_work.model.LatestExchangeRates;
import com.example.alfabank_test_work.client.GiphyFeignClient;
import com.example.alfabank_test_work.client.OpenExchangeRatesFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

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

    public String getGif(String symbols) {

        LocalDate dateYesterday = LocalDate.now().minusDays(1L);

        LatestExchangeRates today = openExchangeRatesFeignClient.getLatest(app_id, symbols);
        LatestExchangeRates yesterday = openExchangeRatesFeignClient.getHistorical(dateYesterday.toString(), app_id, symbols);

        if ( today.getRates().get(symbols) > yesterday.getRates().get(symbols) ) {
            log.info("Курс " + symbols + " по отношению к USD за сегодня стал ниже вчерашнего (tag = broke)");

            GifData gifData = giphyFeignClient.getRandomGif(api_key, broke);

            Map<String, Object> data = gifData.getData();
            Map<String, Object> images = (Map<String, Object>) data.get("images");
            Map<String, String> original = (Map<String, String>) images.get("original");
            String url = original.get("mp4");

            return url;
        }
        else {
            log.info("Курс " + symbols + " по отношению к USD за сегодня стал выше вчерашнего (tag = rich)");

            GifData gifData = giphyFeignClient.getRandomGif(api_key, rich);

            Map<String, Object> data = gifData.getData();
            Map<String, Object> images = (Map<String, Object>) data.get("images");
            Map<String, String> original = (Map<String, String>) images.get("original");
            String url = original.get("mp4");

            return url;
        }
    }
}
