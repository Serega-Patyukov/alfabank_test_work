package com.example.alfabank_test_work.servise;

import com.example.alfabank_test_work.client.GiphyFeignClient;
import com.example.alfabank_test_work.client.OpenExchangeRatesFeignClient;
import com.example.alfabank_test_work.exceptions.AlfaTestWorExceptions;
import com.example.alfabank_test_work.model.LatestExchangeRates;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@WireMockTest(httpPort = 8089)
public class AlfaTestWorkServicesTest {

    /*
    Описание полей там application.properties.
     */

    @Value("${openexchangerates.app.id}")
    private String app_id;
    @Value("${giphy.api.key}")
    private String api_key;
    @Value("${giphy.broke}")
    private String broke;
    @Value("${giphy.rich}")
    private String rich;
    @Value("${openexchangerates.tag.original}")
    private String tagOriginal;
    @Value("${test.gif.rich}")
    private String testGifRich;
    @Value("${test.gif.broke}")
    private String testGifBroke;
    @Value("${warn.error}")
    private String warnError;

    @Autowired
    private GiphyFeignClient giphyFeignClient;
    @Autowired
    private OpenExchangeRatesFeignClient openExchangeRatesFeignClient;
    @Autowired
    private AlfaTestWorkServices alfaTestWorkServices;

    @Test
    void getRandomGifTest() {

        /*
        Этот метод тестирует слой сервиса.

        А точнее метод getRandomGif().

        И поправочка. Метод тестирует только те случаи когда курс валуты в разные дни разный.

        Для успешного выполнения тестов, в application.properties нужно переключить внешиние сервисы на моки.
        Для успешного выполнения тестов должно быть так

            #openexchangerates.url.general=https://openexchangerates.org/api
            openexchangerates.url.general=http://localhost:8089/openexchangerates.org/api

            #giphy.url.general=https://api.giphy.com/v1/gifs
            giphy.url.general=http://localhost:8089/api.giphy.com/v1/gifs

        На внешние сервисы написаны моки.

            Написанные моки:
        - мок, который везвращает json представление курса валюты RUB по отношению к USD за 2022-03-25.
        - мок, который везвращает json представление курса валюты RUB по отношению к USD за 2022-04-25.
        - мок, который везвращает json представление курса валюты RUB по отношению к USD за 2021-12-30.
        - мок, который везвращает json представление курса валюты RUB по отношению к USD за 2022-03-10.
        - мок, который везвращает json представление рандомной гифки с тегом поиска rich.
        - мок, который везвращает json представление рандомной гифки с тегом поиска broke.

        Возвращаемые моками json представления валидны и взяты с соответствующий внешних сервисов.
         */

        /*
        Следующий мок везвращает json представление рандомной гифки с тегом поиска rich.
         */
        stubFor(WireMock.get(urlPathEqualTo("/api.giphy.com/v1/gifs/random"))
                .withQueryParam("api_key", equalTo(api_key))
                .withQueryParam("tag", equalTo(rich))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"data\": {\n" +
                                "        \"type\": \"gif\",\n" +
                                "        \"id\": \"pPDJbbM2pDJfCQHO97\",\n" +
                                "        \"url\": \"https://giphy.com/gifs/ecomity-tt-lunar-new-year-tet-holiday-pPDJbbM2pDJfCQHO97\",\n" +
                                "        \"slug\": \"ecomity-tt-lunar-new-year-tet-holiday-pPDJbbM2pDJfCQHO97\",\n" +
                                "        \"bitly_gif_url\": \"https://gph.is/g/4LX87MP\",\n" +
                                "        \"bitly_url\": \"https://gph.is/g/4LX87MP\",\n" +
                                "        \"embed_url\": \"https://giphy.com/embed/pPDJbbM2pDJfCQHO97\",\n" +
                                "        \"username\": \"ecomity\",\n" +
                                "        \"source\": \"https://ecomity.asia\",\n" +
                                "        \"title\": \"Lunar New Year Money GIF by Ecomity Asia\",\n" +
                                "        \"rating\": \"g\",\n" +
                                "        \"content_url\": \"\",\n" +
                                "        \"source_tld\": \"ecomity.asia\",\n" +
                                "        \"source_post_url\": \"https://ecomity.asia\",\n" +
                                "        \"is_sticker\": 0,\n" +
                                "        \"import_datetime\": \"2021-01-30 04:51:59\",\n" +
                                "        \"trending_datetime\": \"0000-00-00 00:00:00\",\n" +
                                "        \"images\": {\n" +
                                "            \"fixed_width_still\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"size\": \"10796\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200w_s.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200w_s.gif&ct=g\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"preview_gif\": {\n" +
                                "                \"height\": \"360\",\n" +
                                "                \"size\": \"47236\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy-preview.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy-preview.gif&ct=g\",\n" +
                                "                \"width\": \"360\"\n" +
                                "            },\n" +
                                "            \"fixed_height_downsampled\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"size\": \"35339\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200_d.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200_d.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200_d.webp?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200_d.webp&ct=g\",\n" +
                                "                \"webp_size\": \"29892\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"preview\": {\n" +
                                "                \"height\": \"360\",\n" +
                                "                \"mp4\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy-preview.mp4?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy-preview.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"36784\",\n" +
                                "                \"width\": \"360\"\n" +
                                "            },\n" +
                                "            \"fixed_height_small\": {\n" +
                                "                \"height\": \"100\",\n" +
                                "                \"mp4\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/100.mp4?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=100.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"8190\",\n" +
                                "                \"size\": \"13959\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/100.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=100.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/100.webp?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=100.webp&ct=g\",\n" +
                                "                \"webp_size\": \"12590\",\n" +
                                "                \"width\": \"100\"\n" +
                                "            },\n" +
                                "            \"downsized\": {\n" +
                                "                \"height\": \"360\",\n" +
                                "                \"size\": \"59877\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy.gif&ct=g\",\n" +
                                "                \"width\": \"360\"\n" +
                                "            },\n" +
                                "            \"fixed_width_downsampled\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"size\": \"35339\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200w_d.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200w_d.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200w_d.webp?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200w_d.webp&ct=g\",\n" +
                                "                \"webp_size\": \"29892\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"fixed_width\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"mp4\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200w.mp4?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200w.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"18128\",\n" +
                                "                \"size\": \"35339\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200w.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200w.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200w.webp?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200w.webp&ct=g\",\n" +
                                "                \"webp_size\": \"28484\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"downsized_still\": {\n" +
                                "                \"height\": \"360\",\n" +
                                "                \"size\": \"59877\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy_s.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy_s.gif&ct=g\",\n" +
                                "                \"width\": \"360\"\n" +
                                "            },\n" +
                                "            \"downsized_medium\": {\n" +
                                "                \"height\": \"360\",\n" +
                                "                \"size\": \"59877\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy.gif&ct=g\",\n" +
                                "                \"width\": \"360\"\n" +
                                "            },\n" +
                                "            \"original_mp4\": {\n" +
                                "                \"height\": \"480\",\n" +
                                "                \"mp4\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy.mp4?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"46885\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"downsized_large\": {\n" +
                                "                \"height\": \"360\",\n" +
                                "                \"size\": \"59877\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy.gif&ct=g\",\n" +
                                "                \"width\": \"360\"\n" +
                                "            },\n" +
                                "            \"preview_webp\": {\n" +
                                "                \"height\": \"324\",\n" +
                                "                \"size\": \"49576\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy-preview.webp?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy-preview.webp&ct=g\",\n" +
                                "                \"width\": \"324\"\n" +
                                "            },\n" +
                                "            \"original\": {\n" +
                                "                \"frames\": \"4\",\n" +
                                "                \"hash\": \"f0b308598817fb37225becd990e3e5b1\",\n" +
                                "                \"height\": \"360\",\n" +
                                "                \"mp4\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy.mp4?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"46885\",\n" +
                                "                \"size\": \"59877\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy.webp?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy.webp&ct=g\",\n" +
                                "                \"webp_size\": \"55466\",\n" +
                                "                \"width\": \"360\"\n" +
                                "            },\n" +
                                "            \"original_still\": {\n" +
                                "                \"height\": \"360\",\n" +
                                "                \"size\": \"20821\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy_s.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy_s.gif&ct=g\",\n" +
                                "                \"width\": \"360\"\n" +
                                "            },\n" +
                                "            \"fixed_height_small_still\": {\n" +
                                "                \"height\": \"100\",\n" +
                                "                \"size\": \"4160\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/100_s.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=100_s.gif&ct=g\",\n" +
                                "                \"width\": \"100\"\n" +
                                "            },\n" +
                                "            \"fixed_width_small\": {\n" +
                                "                \"height\": \"100\",\n" +
                                "                \"mp4\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/100w.mp4?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=100w.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"8190\",\n" +
                                "                \"size\": \"13959\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/100w.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=100w.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/100w.webp?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=100w.webp&ct=g\",\n" +
                                "                \"webp_size\": \"12590\",\n" +
                                "                \"width\": \"100\"\n" +
                                "            },\n" +
                                "            \"looping\": {\n" +
                                "                \"mp4\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy-loop.mp4?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy-loop.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"741213\"\n" +
                                "            },\n" +
                                "            \"downsized_small\": {\n" +
                                "                \"height\": \"360\",\n" +
                                "                \"mp4\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/giphy-downsized-small.mp4?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=giphy-downsized-small.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"36784\",\n" +
                                "                \"width\": \"360\"\n" +
                                "            },\n" +
                                "            \"fixed_width_small_still\": {\n" +
                                "                \"height\": \"100\",\n" +
                                "                \"size\": \"4160\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/100w_s.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=100w_s.gif&ct=g\",\n" +
                                "                \"width\": \"100\"\n" +
                                "            },\n" +
                                "            \"fixed_height_still\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"size\": \"10796\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200_s.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200_s.gif&ct=g\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"fixed_height\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"mp4\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200.mp4?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"18128\",\n" +
                                "                \"size\": \"35339\",\n" +
                                "                \"url\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200.gif?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media0.giphy.com/media/pPDJbbM2pDJfCQHO97/200.webp?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=200.webp&ct=g\",\n" +
                                "                \"webp_size\": \"28484\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"480w_still\": {\n" +
                                "                \"url\": \"https://media3.giphy.com/media/pPDJbbM2pDJfCQHO97/480w_s.jpg?cid=3392f902a103417684113f8297c436b540c70691696c5c1a&rid=480w_s.jpg&ct=g\",\n" +
                                "                \"width\": \"480\",\n" +
                                "                \"height\": \"480\"\n" +
                                "            }\n" +
                                "        },\n" +
                                "        \"user\": {\n" +
                                "            \"avatar_url\": \"https://media2.giphy.com/avatars/ecomity/FlPuaHFohEVu.jpg\",\n" +
                                "            \"banner_image\": \"https://media2.giphy.com/headers/ecomity/OmT8U8LilwJA.png\",\n" +
                                "            \"banner_url\": \"https://media2.giphy.com/headers/ecomity/OmT8U8LilwJA.png\",\n" +
                                "            \"profile_url\": \"https://giphy.com/ecomity/\",\n" +
                                "            \"username\": \"ecomity\",\n" +
                                "            \"display_name\": \"Ecomity Asia\",\n" +
                                "            \"description\": \"Empower Merchant Community\\r\\nWe provide marketing solutions and promote global trade on e-commerce platforms.\",\n" +
                                "            \"is_verified\": false,\n" +
                                "            \"website_url\": \"https://ecomity.asia/\",\n" +
                                "            \"instagram_url\": \"https://instagram.com/ecomity.asia\"\n" +
                                "        }\n" +
                                "    },\n" +
                                "    \"meta\": {\n" +
                                "        \"msg\": \"OK\",\n" +
                                "        \"status\": 200,\n" +
                                "        \"response_id\": \"a103417684113f8297c436b540c70691696c5c1a\"\n" +
                                "    }\n" +
                                "}")
                ));

        /*
        Следующий мок везвращает json представление рандомной гифки с тегом поиска broke.
         */
        stubFor(WireMock.get(urlPathEqualTo("/api.giphy.com/v1/gifs/random"))
                .withQueryParam("api_key", equalTo(api_key))
                .withQueryParam("tag", equalTo(broke))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"data\": {\n" +
                                "        \"type\": \"gif\",\n" +
                                "        \"id\": \"26uffbT0DVdF6Reec\",\n" +
                                "        \"url\": \"https://giphy.com/gifs/southparkgifs-26uffbT0DVdF6Reec\",\n" +
                                "        \"slug\": \"southparkgifs-26uffbT0DVdF6Reec\",\n" +
                                "        \"bitly_gif_url\": \"http://gph.is/2b8Btmu\",\n" +
                                "        \"bitly_url\": \"http://gph.is/2b8Btmu\",\n" +
                                "        \"embed_url\": \"https://giphy.com/embed/26uffbT0DVdF6Reec\",\n" +
                                "        \"username\": \"southpark\",\n" +
                                "        \"source\": \"http://comedycentral.com\",\n" +
                                "        \"title\": \"protest engage GIF by South Park \",\n" +
                                "        \"rating\": \"pg-13\",\n" +
                                "        \"content_url\": \"\",\n" +
                                "        \"source_tld\": \"comedycentral.com\",\n" +
                                "        \"source_post_url\": \"http://comedycentral.com\",\n" +
                                "        \"is_sticker\": 0,\n" +
                                "        \"import_datetime\": \"2016-08-20 19:37:12\",\n" +
                                "        \"trending_datetime\": \"1970-01-01 00:00:00\",\n" +
                                "        \"images\": {\n" +
                                "            \"fixed_width_still\": {\n" +
                                "                \"height\": \"113\",\n" +
                                "                \"size\": \"10916\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w_s.gif&ct=g\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"preview_gif\": {\n" +
                                "                \"height\": \"89\",\n" +
                                "                \"size\": \"49552\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-preview.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-preview.gif&ct=g\",\n" +
                                "                \"width\": \"158\"\n" +
                                "            },\n" +
                                "            \"fixed_height_downsampled\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"size\": \"92539\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200_d.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200_d.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200_d.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200_d.webp&ct=g\",\n" +
                                "                \"webp_size\": \"71966\",\n" +
                                "                \"width\": \"356\"\n" +
                                "            },\n" +
                                "            \"preview\": {\n" +
                                "                \"height\": \"216\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-preview.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-preview.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"23737\",\n" +
                                "                \"width\": \"384\"\n" +
                                "            },\n" +
                                "            \"fixed_height_small\": {\n" +
                                "                \"height\": \"100\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"15601\",\n" +
                                "                \"size\": \"105220\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100.webp&ct=g\",\n" +
                                "                \"webp_size\": \"94652\",\n" +
                                "                \"width\": \"178\"\n" +
                                "            },\n" +
                                "            \"downsized\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"fixed_width_downsampled\": {\n" +
                                "                \"height\": \"113\",\n" +
                                "                \"size\": \"40958\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w_d.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w_d.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w_d.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w_d.webp&ct=g\",\n" +
                                "                \"webp_size\": \"34378\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"fixed_width\": {\n" +
                                "                \"height\": \"113\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"17624\",\n" +
                                "                \"size\": \"102248\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w.webp&ct=g\",\n" +
                                "                \"webp_size\": \"96436\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"downsized_still\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy_s.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"downsized_medium\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"original_mp4\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"146126\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"downsized_large\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"preview_webp\": {\n" +
                                "                \"height\": \"116\",\n" +
                                "                \"size\": \"42498\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-preview.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-preview.webp&ct=g\",\n" +
                                "                \"width\": \"206\"\n" +
                                "            },\n" +
                                "            \"original\": {\n" +
                                "                \"frames\": \"37\",\n" +
                                "                \"hash\": \"13a23450070103fb41c0923da7d31ae4\",\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"146126\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.webp&ct=g\",\n" +
                                "                \"webp_size\": \"382880\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"original_still\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"51622\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy_s.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"fixed_height_small_still\": {\n" +
                                "                \"height\": \"100\",\n" +
                                "                \"size\": \"9214\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100_s.gif&ct=g\",\n" +
                                "                \"width\": \"178\"\n" +
                                "            },\n" +
                                "            \"fixed_width_small\": {\n" +
                                "                \"height\": \"57\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100w.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100w.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"6895\",\n" +
                                "                \"size\": \"40428\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100w.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100w.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100w.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100w.webp&ct=g\",\n" +
                                "                \"webp_size\": \"27504\",\n" +
                                "                \"width\": \"100\"\n" +
                                "            },\n" +
                                "            \"looping\": {\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-loop.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-loop.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"857607\"\n" +
                                "            },\n" +
                                "            \"downsized_small\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-downsized-small.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-downsized-small.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"146126\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"fixed_width_small_still\": {\n" +
                                "                \"height\": \"57\",\n" +
                                "                \"size\": \"4019\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100w_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100w_s.gif&ct=g\",\n" +
                                "                \"width\": \"100\"\n" +
                                "            },\n" +
                                "            \"fixed_height_still\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"size\": \"19311\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200_s.gif&ct=g\",\n" +
                                "                \"width\": \"356\"\n" +
                                "            },\n" +
                                "            \"fixed_height\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"54289\",\n" +
                                "                \"size\": \"269204\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200.webp&ct=g\",\n" +
                                "                \"webp_size\": \"262698\",\n" +
                                "                \"width\": \"356\"\n" +
                                "            },\n" +
                                "            \"480w_still\": {\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/480w_s.jpg?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=480w_s.jpg&ct=g\",\n" +
                                "                \"width\": \"480\",\n" +
                                "                \"height\": \"270\"\n" +
                                "            }\n" +
                                "        },\n" +
                                "        \"user\": {\n" +
                                "            \"avatar_url\": \"https://media0.giphy.com/channel_assets/southparkgifs/Yxjwn4anI9bQ.jpg\",\n" +
                                "            \"banner_image\": \"https://media0.giphy.com/channel_assets/southparkgifs/F6LQFfrfV5e2.jpg\",\n" +
                                "            \"banner_url\": \"https://media0.giphy.com/channel_assets/southparkgifs/F6LQFfrfV5e2.jpg\",\n" +
                                "            \"profile_url\": \"https://giphy.com/southpark/\",\n" +
                                "            \"username\": \"southpark\",\n" +
                                "            \"display_name\": \"South Park\",\n" +
                                "            \"description\": \"Every GIF from every episode of South Park. Find and share all of your favorite characters, moments, and reactions.\",\n" +
                                "            \"is_verified\": true,\n" +
                                "            \"website_url\": \"http://southpark.cc.com/\",\n" +
                                "            \"instagram_url\": \"https://instagram.com/SouthPark\"\n" +
                                "        }\n" +
                                "    },\n" +
                                "    \"meta\": {\n" +
                                "        \"msg\": \"OK\",\n" +
                                "        \"status\": 200,\n" +
                                "        \"response_id\": \"1803772bfa85cf6db6ff8935272c477323309f23\"\n" +
                                "    }\n" +
                                "}")
                ));

        /*
        Следующий мок везвращает json представление курса валюты RUB по отношению к USD за 2022-03-25.
         */
        stubFor(WireMock.get(urlPathEqualTo("/openexchangerates.org/api/historical/2022-03-25.json"))
                .withId(UUID.randomUUID())
                .withQueryParam("app_id", equalTo(app_id))
                .withQueryParam("symbols", equalTo(tagOriginal))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                                "    \"timestamp\": 1648252740,\n" +
                                "    \"base\": \"USD\",\n" +
                                "    \"rates\": {\n" +
                                "        \"RUB\": 102\n" +
                                "    }\n" +
                                "}")
                ));

        /*
        Следующий мок везвращает json представление курса валюты RUB по отношению к USD за 2022-04-25.
         */
        stubFor(WireMock.get(urlPathEqualTo("/openexchangerates.org/api/historical/2022-04-25.json"))
                .withId(UUID.randomUUID())
                .withQueryParam("app_id", equalTo(app_id))
                .withQueryParam("symbols", equalTo(tagOriginal))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                                "    \"timestamp\": 1650931140,\n" +
                                "    \"base\": \"USD\",\n" +
                                "    \"rates\": {\n" +
                                "        \"RUB\": 75.149994\n" +
                                "    }\n" +
                                "}")
                ));


        /*
        data1 - курс RUB по отношению к USD за 2022-03-25.
        data2 - курс RUB по отношению к USD за 2022-04-25.
        Между выбранными датами курс RUB растет.
         */
        LatestExchangeRates data1 = openExchangeRatesFeignClient.getHistorical("2022-03-25", app_id, tagOriginal);
        LatestExchangeRates data2 = openExchangeRatesFeignClient.getHistorical("2022-04-25", app_id, tagOriginal);
        Assertions.assertEquals(alfaTestWorkServices.getRandomGif(data2, data1, tagOriginal), testGifRich);

        /*
        Следующий мок везвращает json представление курса валюты RUB по отношению к USD за 2022-12-30.
         */
        stubFor(WireMock.get(urlPathEqualTo("/openexchangerates.org/api/historical/2021-12-30.json"))
                .withId(UUID.randomUUID())
                .withQueryParam("app_id", equalTo(app_id))
                .withQueryParam("symbols", equalTo(tagOriginal))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                                "    \"timestamp\": 1640908775,\n" +
                                "    \"base\": \"USD\",\n" +
                                "    \"rates\": {\n" +
                                "        \"RUB\": 74.5665\n" +
                                "    }\n" +
                                "}")
                ));

        /*
        Следующий мок везвращает json представление курса валюты RUB по отношению к USD за 2022-03-10.
         */
        stubFor(WireMock.get(urlPathEqualTo("/openexchangerates.org/api/historical/2022-03-10.json"))
                .withId(UUID.randomUUID())
                .withQueryParam("app_id", equalTo(app_id))
                .withQueryParam("symbols", equalTo(tagOriginal))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                                "    \"timestamp\": 1646956775,\n" +
                                "    \"base\": \"USD\",\n" +
                                "    \"rates\": {\n" +
                                "        \"RUB\": 133.5\n" +
                                "    }\n" +
                                "}")
                ));

        /*
        data1 - курс RUB по отношению к USD за 2021-12-30.
        data2 - курс RUB по отношению к USD за 2022-03-10.
        Между выбранными датами курс RUB падает.
         */
        LatestExchangeRates data3 = openExchangeRatesFeignClient.getHistorical("2021-12-30", app_id, tagOriginal);
        LatestExchangeRates data4 = openExchangeRatesFeignClient.getHistorical("2022-03-10", app_id, tagOriginal);
        Assertions.assertEquals(alfaTestWorkServices.getRandomGif(data4, data3, tagOriginal), testGifBroke);
    }

    @Test
    void getOriginalUrlGifTest() {

        /*
        Этот метод тестирует слой сервиса.

        А точнее метод getOriginalUrlGif().

        И поправочка. Метод тестирует только на NullPointerException.

        Для успешного выполнения тестов, в application.properties нужно переключить внешиние сервисы на моки.
        Для успешного выполнения тестов должно быть так

            #openexchangerates.url.general=https://openexchangerates.org/api
            openexchangerates.url.general=http://localhost:8089/openexchangerates.org/api

            #giphy.url.general=https://api.giphy.com/v1/gifs
            giphy.url.general=http://localhost:8089/api.giphy.com/v1/gifs

        На внешние сервисы написаны моки.

            Написанные моки:
        - мок, который везвращает json представление рандомной гифки с тегом поиска broke.

        Возвращаемые моками json представления валидны и взяты с соответствующий внешних сервисов.
         */

        /*
        Следующий мок везвращает json представление рандомной сломанной гифки с тегом поиска broke.
         */
        stubFor(WireMock.get(urlPathEqualTo("/api.giphy.com/v1/gifs/random"))
                .withQueryParam("api_key", equalTo(api_key))
                .withQueryParam("tag", equalTo(broke))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"data\": {\n" +
                                "        \"type\": \"gif\",\n" +
                                "        \"id\": \"26uffbT0DVdF6Reec\",\n" +
                                "        \"url\": \"https://giphy.com/gifs/southparkgifs-26uffbT0DVdF6Reec\",\n" +
                                "        \"slug\": \"southparkgifs-26uffbT0DVdF6Reec\",\n" +
                                "        \"bitly_gif_url\": \"http://gph.is/2b8Btmu\",\n" +
                                "        \"bitly_url\": \"http://gph.is/2b8Btmu\",\n" +
                                "        \"embed_url\": \"https://giphy.com/embed/26uffbT0DVdF6Reec\",\n" +
                                "        \"username\": \"southpark\",\n" +
                                "        \"source\": \"http://comedycentral.com\",\n" +
                                "        \"title\": \"protest engage GIF by South Park \",\n" +
                                "        \"rating\": \"pg-13\",\n" +
                                "        \"content_url\": \"\",\n" +
                                "        \"source_tld\": \"comedycentral.com\",\n" +
                                "        \"source_post_url\": \"http://comedycentral.com\",\n" +
                                "        \"is_sticker\": 0,\n" +
                                "        \"import_datetime\": \"2016-08-20 19:37:12\",\n" +
                                "        \"trending_datetime\": \"1970-01-01 00:00:00\",\n" +
                                "        \"images\": {\n" +
                                "            \"fixed_width_still\": {\n" +
                                "                \"height\": \"113\",\n" +
                                "                \"size\": \"10916\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w_s.gif&ct=g\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"preview_gif\": {\n" +
                                "                \"height\": \"89\",\n" +
                                "                \"size\": \"49552\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-preview.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-preview.gif&ct=g\",\n" +
                                "                \"width\": \"158\"\n" +
                                "            },\n" +
                                "            \"fixed_height_downsampled\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"size\": \"92539\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200_d.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200_d.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200_d.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200_d.webp&ct=g\",\n" +
                                "                \"webp_size\": \"71966\",\n" +
                                "                \"width\": \"356\"\n" +
                                "            },\n" +
                                "            \"preview\": {\n" +
                                "                \"height\": \"216\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-preview.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-preview.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"23737\",\n" +
                                "                \"width\": \"384\"\n" +
                                "            },\n" +
                                "            \"fixed_height_small\": {\n" +
                                "                \"height\": \"100\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"15601\",\n" +
                                "                \"size\": \"105220\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100.webp&ct=g\",\n" +
                                "                \"webp_size\": \"94652\",\n" +
                                "                \"width\": \"178\"\n" +
                                "            },\n" +
                                "            \"downsized\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"fixed_width_downsampled\": {\n" +
                                "                \"height\": \"113\",\n" +
                                "                \"size\": \"40958\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w_d.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w_d.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w_d.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w_d.webp&ct=g\",\n" +
                                "                \"webp_size\": \"34378\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"fixed_width\": {\n" +
                                "                \"height\": \"113\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"17624\",\n" +
                                "                \"size\": \"102248\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200w.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200w.webp&ct=g\",\n" +
                                "                \"webp_size\": \"96436\",\n" +
                                "                \"width\": \"200\"\n" +
                                "            },\n" +
                                "            \"downsized_still\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy_s.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"downsized_medium\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"original_mp4\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"146126\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"downsized_large\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"preview_webp\": {\n" +
                                "                \"height\": \"116\",\n" +
                                "                \"size\": \"42498\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-preview.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-preview.webp&ct=g\",\n" +
                                "                \"width\": \"206\"\n" +
                                "            },\n" +
                                "            \"originall\": {\n" +
                                "                \"frames\": \"37\",\n" +
                                "                \"hash\": \"13a23450070103fb41c0923da7d31ae4\",\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"146126\",\n" +
                                "                \"size\": \"393949\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy.webp&ct=g\",\n" +
                                "                \"webp_size\": \"382880\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"original_still\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"size\": \"51622\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy_s.gif&ct=g\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"fixed_height_small_still\": {\n" +
                                "                \"height\": \"100\",\n" +
                                "                \"size\": \"9214\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100_s.gif&ct=g\",\n" +
                                "                \"width\": \"178\"\n" +
                                "            },\n" +
                                "            \"fixed_width_small\": {\n" +
                                "                \"height\": \"57\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100w.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100w.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"6895\",\n" +
                                "                \"size\": \"40428\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100w.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100w.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100w.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100w.webp&ct=g\",\n" +
                                "                \"webp_size\": \"27504\",\n" +
                                "                \"width\": \"100\"\n" +
                                "            },\n" +
                                "            \"looping\": {\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-loop.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-loop.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"857607\"\n" +
                                "            },\n" +
                                "            \"downsized_small\": {\n" +
                                "                \"height\": \"270\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/giphy-downsized-small.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=giphy-downsized-small.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"146126\",\n" +
                                "                \"width\": \"480\"\n" +
                                "            },\n" +
                                "            \"fixed_width_small_still\": {\n" +
                                "                \"height\": \"57\",\n" +
                                "                \"size\": \"4019\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/100w_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=100w_s.gif&ct=g\",\n" +
                                "                \"width\": \"100\"\n" +
                                "            },\n" +
                                "            \"fixed_height_still\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"size\": \"19311\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200_s.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200_s.gif&ct=g\",\n" +
                                "                \"width\": \"356\"\n" +
                                "            },\n" +
                                "            \"fixed_height\": {\n" +
                                "                \"height\": \"200\",\n" +
                                "                \"mp4\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200.mp4?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200.mp4&ct=g\",\n" +
                                "                \"mp4_size\": \"54289\",\n" +
                                "                \"size\": \"269204\",\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200.gif?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200.gif&ct=g\",\n" +
                                "                \"webp\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/200.webp?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=200.webp&ct=g\",\n" +
                                "                \"webp_size\": \"262698\",\n" +
                                "                \"width\": \"356\"\n" +
                                "            },\n" +
                                "            \"480w_still\": {\n" +
                                "                \"url\": \"https://media1.giphy.com/media/26uffbT0DVdF6Reec/480w_s.jpg?cid=3392f9021803772bfa85cf6db6ff8935272c477323309f23&rid=480w_s.jpg&ct=g\",\n" +
                                "                \"width\": \"480\",\n" +
                                "                \"height\": \"270\"\n" +
                                "            }\n" +
                                "        },\n" +
                                "        \"user\": {\n" +
                                "            \"avatar_url\": \"https://media0.giphy.com/channel_assets/southparkgifs/Yxjwn4anI9bQ.jpg\",\n" +
                                "            \"banner_image\": \"https://media0.giphy.com/channel_assets/southparkgifs/F6LQFfrfV5e2.jpg\",\n" +
                                "            \"banner_url\": \"https://media0.giphy.com/channel_assets/southparkgifs/F6LQFfrfV5e2.jpg\",\n" +
                                "            \"profile_url\": \"https://giphy.com/southpark/\",\n" +
                                "            \"username\": \"southpark\",\n" +
                                "            \"display_name\": \"South Park\",\n" +
                                "            \"description\": \"Every GIF from every episode of South Park. Find and share all of your favorite characters, moments, and reactions.\",\n" +
                                "            \"is_verified\": true,\n" +
                                "            \"website_url\": \"http://southpark.cc.com/\",\n" +
                                "            \"instagram_url\": \"https://instagram.com/SouthPark\"\n" +
                                "        }\n" +
                                "    },\n" +
                                "    \"meta\": {\n" +
                                "        \"msg\": \"OK\",\n" +
                                "        \"status\": 200,\n" +
                                "        \"response_id\": \"1803772bfa85cf6db6ff8935272c477323309f23\"\n" +
                                "    }\n" +
                                "}")
                ));

        String messageNullPointerExc = "";

        try {
            alfaTestWorkServices.getOriginalUrlGif(giphyFeignClient.getRandomGif(api_key, broke));
        } catch (AlfaTestWorExceptions alfaTestWorExceptions) {
            messageNullPointerExc = alfaTestWorExceptions.getMessage();
        }

        Assertions.assertEquals(messageNullPointerExc, warnError);
    }
}