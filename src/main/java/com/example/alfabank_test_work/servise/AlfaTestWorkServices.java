package com.example.alfabank_test_work.servise;

import com.example.alfabank_test_work.model.LatestExchangeRates;
import com.example.alfabank_test_work.utils.GiphyFeignClient;
import com.example.alfabank_test_work.utils.OpenExchangeRatesFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlfaTestWorkServices {

    private final OpenExchangeRatesFeignClient openExchangeRatesFeignClient;
    private final GiphyFeignClient giphyFeignClient;

    public Object getLatest(String symbols) {

        LocalDate dateYesterday = LocalDate.now().minusDays(1L);

        LatestExchangeRates today = openExchangeRatesFeignClient.getLatest("15a1bdf2a1084b188b6a4db2412e28f1", symbols);
        LatestExchangeRates yesterday = openExchangeRatesFeignClient.getHistorical(dateYesterday.toString() + ".json", "15a1bdf2a1084b188b6a4db2412e28f1", symbols);

        if ( today.getRates().get(symbols) > yesterday.getRates().get(symbols) ) {
            log.info("Курс " + symbols + " по отношению к USD за сегодня стал ниже вчерашнего (broke)");
            return giphyFeignClient.getBroke("zpnKEjgfKpFksw0FJv9D3gkOK6OT1ZF0", "broke");
        }
        else {
            log.info("Курс " + symbols + " по отношению к USD за сегодня стал выше вчерашнего (rich)");
            return giphyFeignClient.getRich("zpnKEjgfKpFksw0FJv9D3gkOK6OT1ZF0", "rich");
        }
    }
}
