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
import java.util.Optional;

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

        // Получаем вчерашнюю дату.
        LocalDate dateYesterday = LocalDate.now().minusDays(1L);

        // Курс по отношению к USD за сегодня.
        LatestExchangeRates today = openExchangeRatesFeignClient.getLatest(app_id, symbols);
        // Курс по отношению к USD за вчера.
        LatestExchangeRates yesterday = openExchangeRatesFeignClient.getHistorical(dateYesterday.toString(), app_id, symbols);

        return getRandomGif(today, yesterday, symbols);
    }
    
    public String getRandomGif(LatestExchangeRates today, LatestExchangeRates yesterday, String symbols) {
        if ( today.getRates().get(symbols) > yesterday.getRates().get(symbols) ) {
            log.info("Курс " + symbols + " по отношению к USD за сегодня стал ниже вчерашнего (tag = broke)");
            return getOriginalUrlGif(giphyFeignClient.getRandomGif(api_key, broke));
        }
        else {
            log.info("Курс " + symbols + " по отношению к USD за сегодня стал выше вчерашнего (tag = rich)");
            return getOriginalUrlGif(giphyFeignClient.getRandomGif(api_key, rich));
        }
    }

    public String getOriginalUrlGif(GifData gifData) {

        Optional<GifData> data = Optional.ofNullable(gifData);

        String url = data
                .map(gifData1 -> (Map<String, Object>) gifData1.getData().get("images"))
                .map(stringObjectMap -> (Map<String, Object>) stringObjectMap.get("original"))
                .map(stringObjectMap -> (String)  stringObjectMap.get("webp"))
                .get();

//        Map<String, Object> data = gifData.getData();
//        Map<String, Object> images = (Map<String, Object>) data.get("images");
//        Map<String, String> original = (Map<String, String>) images.get("original");
//        String url = original.get("webp");

        return url;
    }
}