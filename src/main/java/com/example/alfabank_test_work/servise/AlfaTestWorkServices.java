package com.example.alfabank_test_work.servise;

import com.example.alfabank_test_work.exceptions.AlfaTestWorExceptions;
import com.example.alfabank_test_work.model.GifData;
import com.example.alfabank_test_work.model.LatestExchangeRates;
import com.example.alfabank_test_work.client.GiphyFeignClient;
import com.example.alfabank_test_work.client.OpenExchangeRatesFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlfaTestWorkServices {

    /*
    Описание полей там application.properties.
     */

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
    @Value("${warn.message}")
    private String warnMessage;
    @Value("${warn.error}")
    private String warnError;

    public String getGif(String symbols) {

        // Получаем вчерашнюю дату.
        LocalDate dateYesterday = LocalDate.now().minusDays(1L);

        // Курс по отношению к USD за сегодня.
        LatestExchangeRates today = openExchangeRatesFeignClient.getLatest(app_id, symbols);
        // Курс по отношению к USD за вчера.
        LatestExchangeRates yesterday = openExchangeRatesFeignClient.getHistorical(dateYesterday.toString(), app_id, symbols);

        // Тут вернем ссылку на гифку.
        return getRandomGif(today, yesterday, symbols);
    }
    
    public String getRandomGif(LatestExchangeRates today, LatestExchangeRates yesterday, String symbols) {

        // Сравниваем значение курса валют за сегодня и вчера. И рандомно возвращаем соответствующую гифку.
        if ( today.getRates().get(symbols) > yesterday.getRates().get(symbols) ) {
            return getOriginalUrlGif(giphyFeignClient.getRandomGif(api_key, broke));
        }
        else if (today.getRates().get(symbols) < yesterday.getRates().get(symbols)) {
            return getOriginalUrlGif(giphyFeignClient.getRandomGif(api_key, rich));
        }
        else {   // Если курсы валют равны, то вернем соответствующее сообщение.
            log.warn(warnMessage);
            throw new AlfaTestWorExceptions(warnMessage);
        }
    }

    public String getOriginalUrlGif(GifData gifData) {

        // Достаем и json представления ссылку на гифку и возвращаем ее.

        String url = "";

        try {
            Map<String, Object> data = gifData.getData();
            Map<String, Object> images = (Map<String, Object>) data.get("images");
            Map<String, String> original = (Map<String, String>) images.get("original");
            url = original.get("webp");
        } catch (NullPointerException nullPointerException) {
            log.warn(warnError);
            throw new AlfaTestWorExceptions(warnError);
        }

        return url;
    }
}