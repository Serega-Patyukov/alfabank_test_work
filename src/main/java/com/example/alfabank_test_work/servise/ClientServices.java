package com.example.alfabank_test_work.servise;

import com.example.alfabank_test_work.model.Latest;
import com.example.alfabank_test_work.utils.JsonFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServices {

    private final JsonFeignClient jsonFeignClient;

    public Latest getLatest(String symbols) {

        LocalDate dateYesterday = LocalDate.now().minusDays(1L);

        Latest today = jsonFeignClient.getLatest("15a1bdf2a1084b188b6a4db2412e28f1", symbols);
        Latest yesterday = jsonFeignClient.getHistorical(dateYesterday.toString() + ".json", "15a1bdf2a1084b188b6a4db2412e28f1", symbols);

        if ( today.getRates().get(symbols) > yesterday.getRates().get(symbols) ) {
            log.info("Курс по отношению к USD за сегодня стал выше вчерашнего");
        }
        else {
            log.info("Курс по отношению к USD за сегодня стал ниже вчерашнего");
        }

        return today;
    }
}
