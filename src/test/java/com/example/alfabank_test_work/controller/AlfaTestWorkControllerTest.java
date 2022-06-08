package com.example.alfabank_test_work.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8089)
public class AlfaTestWorkControllerTest {

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

    @Value("${openexchangerates.tag0}")
    private String tag0;
    @Value("${openexchangerates.tag1}")
    private String tag1;
    @Value("${openexchangerates.tag2}")
    private String tag2;
    @Value("${openexchangerates.tag.original}")
    private String tagOriginal;

    @Value("${warn.message}")
    private String warnMessage;

    @Autowired
    private MockMvc mockMvc;

    // Эта переменная нужна для корректного формирования пути мока. Далее в моках есть описание.
    LocalDate localDate = LocalDate.now().minusDays(1L);

    @Test
    void getGifTest() throws Exception {

        /*
        Этот метод тестирует контроллер, в который передается валюта.

        Для успешного выполнения тестов, в application.properties нужно переключить внешние сервисы на моки.
        Для успешного выполнения тестов должно быть так

            #openexchangerates.url.general=https://openexchangerates.org/api
            openexchangerates.url.general=http://localhost:8089/openexchangerates.org/api

            #giphy.url.general=https://api.giphy.com/v1/gifs
            giphy.url.general=http://localhost:8089/api.giphy.com/v1/gifs

            В контроллер предается:
        - пустая строка;
        - любая строка, но не являющаяся валютой;
        - строка являющаяся валютой.

        На внешние сервисы написаны моки.

            Написанные моки:
        - мок, который возвращает json представление курса валюты RUB по отношению к USD за 2022-06-05.
        - мок, который возвращает json представление курса валюты RUB по отношению к USD за 2022-06-04.
        - мок, который возвращает json представление курса валюты RUB по отношению к USD за 2022-06-02.
        - мок, который возвращает json представление доступных валют за 2022-06-05.
        - мок, который возвращает json представление рандомной гифки с тегом поиска rich.
        - мок, который возвращает json представление рандомной гифки с тегом поиска broke.

        Возвращаемые моками json представления валидны и взяты с соответствующих внешних сервисов.

        Так получилось, что курс валюты RUB по отношению к USD за 2022-06-05 и за 2022-06-04 равны.
        Эта ситуация тут тоже тестируется.

        Также будут протестированы дни с разными значениями курса валюты за 2022-06-05 и 2022-06-02.
         */

        /*
        Следующий мок возвращает json представление курса валюты RUB по отношению к USD за 2022-06-05.
         */
        stubFor(WireMock.get(urlPathEqualTo("/openexchangerates.org/api/latest.json"))
                .withId(UUID.randomUUID())
                .withQueryParam("app_id", equalTo(app_id))
                .withQueryParam("symbols", equalTo(tagOriginal))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                                "    \"timestamp\": 1654444768,\n" +
                                "    \"base\": \"USD\",\n" +
                                "    \"rates\": {\n" +
                                "        \"RUB\": 63.360006\n" +
                                "    }\n" +
                                "}")
                ));

        /*
        Следующий мок возвращает json представление курса валюты RUB по отношению к USD за 2022-06-04.

        Согласно логике слоя сервиса, вместо переменной localDate в адрес этого мока будет приходить вчерашняя дата.
        Поэтому переменная localDate всегда будет содержать вчерашнюю дату.
        И этот мок всегда будет возвращать курса валюты RUB по отношению к USD за 2022-06-04.
         */
        stubFor(WireMock.get(urlPathEqualTo("/openexchangerates.org/api/historical/" + localDate + ".json"))
                .withId(UUID.randomUUID())
                .withQueryParam("app_id", equalTo(app_id))
                .withQueryParam("symbols", equalTo(tagOriginal))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                                "    \"timestamp\": 1654387140,\n" +
                                "    \"base\": \"USD\",\n" +
                                "    \"rates\": {\n" +
                                "        \"RUB\": 63.360006\n" +
                                "    }\n" +
                                "}")
                ));

        /*
        Следующий мок возвращает json представление доступных валют за 2022-06-05.
         */
        stubFor(WireMock.get(urlPathEqualTo("/openexchangerates.org/api/currencies.json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"AED\": \"United Arab Emirates Dirham\",\n" +
                                "    \"AFN\": \"Afghan Afghani\",\n" +
                                "    \"ALL\": \"Albanian Lek\",\n" +
                                "    \"AMD\": \"Armenian Dram\",\n" +
                                "    \"ANG\": \"Netherlands Antillean Guilder\",\n" +
                                "    \"AOA\": \"Angolan Kwanza\",\n" +
                                "    \"ARS\": \"Argentine Peso\",\n" +
                                "    \"AUD\": \"Australian Dollar\",\n" +
                                "    \"AWG\": \"Aruban Florin\",\n" +
                                "    \"AZN\": \"Azerbaijani Manat\",\n" +
                                "    \"BAM\": \"Bosnia-Herzegovina Convertible Mark\",\n" +
                                "    \"BBD\": \"Barbadian Dollar\",\n" +
                                "    \"BDT\": \"Bangladeshi Taka\",\n" +
                                "    \"BGN\": \"Bulgarian Lev\",\n" +
                                "    \"BHD\": \"Bahraini Dinar\",\n" +
                                "    \"BIF\": \"Burundian Franc\",\n" +
                                "    \"BMD\": \"Bermudan Dollar\",\n" +
                                "    \"BND\": \"Brunei Dollar\",\n" +
                                "    \"BOB\": \"Bolivian Boliviano\",\n" +
                                "    \"BRL\": \"Brazilian Real\",\n" +
                                "    \"BSD\": \"Bahamian Dollar\",\n" +
                                "    \"BTC\": \"Bitcoin\",\n" +
                                "    \"BTN\": \"Bhutanese Ngultrum\",\n" +
                                "    \"BWP\": \"Botswanan Pula\",\n" +
                                "    \"BYN\": \"Belarusian Ruble\",\n" +
                                "    \"BZD\": \"Belize Dollar\",\n" +
                                "    \"CAD\": \"Canadian Dollar\",\n" +
                                "    \"CDF\": \"Congolese Franc\",\n" +
                                "    \"CHF\": \"Swiss Franc\",\n" +
                                "    \"CLF\": \"Chilean Unit of Account (UF)\",\n" +
                                "    \"CLP\": \"Chilean Peso\",\n" +
                                "    \"CNH\": \"Chinese Yuan (Offshore)\",\n" +
                                "    \"CNY\": \"Chinese Yuan\",\n" +
                                "    \"COP\": \"Colombian Peso\",\n" +
                                "    \"CRC\": \"Costa Rican Colón\",\n" +
                                "    \"CUC\": \"Cuban Convertible Peso\",\n" +
                                "    \"CUP\": \"Cuban Peso\",\n" +
                                "    \"CVE\": \"Cape Verdean Escudo\",\n" +
                                "    \"CZK\": \"Czech Republic Koruna\",\n" +
                                "    \"DJF\": \"Djiboutian Franc\",\n" +
                                "    \"DKK\": \"Danish Krone\",\n" +
                                "    \"DOP\": \"Dominican Peso\",\n" +
                                "    \"DZD\": \"Algerian Dinar\",\n" +
                                "    \"EGP\": \"Egyptian Pound\",\n" +
                                "    \"ERN\": \"Eritrean Nakfa\",\n" +
                                "    \"ETB\": \"Ethiopian Birr\",\n" +
                                "    \"EUR\": \"Euro\",\n" +
                                "    \"FJD\": \"Fijian Dollar\",\n" +
                                "    \"FKP\": \"Falkland Islands Pound\",\n" +
                                "    \"GBP\": \"British Pound Sterling\",\n" +
                                "    \"GEL\": \"Georgian Lari\",\n" +
                                "    \"GGP\": \"Guernsey Pound\",\n" +
                                "    \"GHS\": \"Ghanaian Cedi\",\n" +
                                "    \"GIP\": \"Gibraltar Pound\",\n" +
                                "    \"GMD\": \"Gambian Dalasi\",\n" +
                                "    \"GNF\": \"Guinean Franc\",\n" +
                                "    \"GTQ\": \"Guatemalan Quetzal\",\n" +
                                "    \"GYD\": \"Guyanaese Dollar\",\n" +
                                "    \"HKD\": \"Hong Kong Dollar\",\n" +
                                "    \"HNL\": \"Honduran Lempira\",\n" +
                                "    \"HRK\": \"Croatian Kuna\",\n" +
                                "    \"HTG\": \"Haitian Gourde\",\n" +
                                "    \"HUF\": \"Hungarian Forint\",\n" +
                                "    \"IDR\": \"Indonesian Rupiah\",\n" +
                                "    \"ILS\": \"Israeli New Sheqel\",\n" +
                                "    \"IMP\": \"Manx pound\",\n" +
                                "    \"INR\": \"Indian Rupee\",\n" +
                                "    \"IQD\": \"Iraqi Dinar\",\n" +
                                "    \"IRR\": \"Iranian Rial\",\n" +
                                "    \"ISK\": \"Icelandic Króna\",\n" +
                                "    \"JEP\": \"Jersey Pound\",\n" +
                                "    \"JMD\": \"Jamaican Dollar\",\n" +
                                "    \"JOD\": \"Jordanian Dinar\",\n" +
                                "    \"JPY\": \"Japanese Yen\",\n" +
                                "    \"KES\": \"Kenyan Shilling\",\n" +
                                "    \"KGS\": \"Kyrgystani Som\",\n" +
                                "    \"KHR\": \"Cambodian Riel\",\n" +
                                "    \"KMF\": \"Comorian Franc\",\n" +
                                "    \"KPW\": \"North Korean Won\",\n" +
                                "    \"KRW\": \"South Korean Won\",\n" +
                                "    \"KWD\": \"Kuwaiti Dinar\",\n" +
                                "    \"KYD\": \"Cayman Islands Dollar\",\n" +
                                "    \"KZT\": \"Kazakhstani Tenge\",\n" +
                                "    \"LAK\": \"Laotian Kip\",\n" +
                                "    \"LBP\": \"Lebanese Pound\",\n" +
                                "    \"LKR\": \"Sri Lankan Rupee\",\n" +
                                "    \"LRD\": \"Liberian Dollar\",\n" +
                                "    \"LSL\": \"Lesotho Loti\",\n" +
                                "    \"LYD\": \"Libyan Dinar\",\n" +
                                "    \"MAD\": \"Moroccan Dirham\",\n" +
                                "    \"MDL\": \"Moldovan Leu\",\n" +
                                "    \"MGA\": \"Malagasy Ariary\",\n" +
                                "    \"MKD\": \"Macedonian Denar\",\n" +
                                "    \"MMK\": \"Myanma Kyat\",\n" +
                                "    \"MNT\": \"Mongolian Tugrik\",\n" +
                                "    \"MOP\": \"Macanese Pataca\",\n" +
                                "    \"MRU\": \"Mauritanian Ouguiya\",\n" +
                                "    \"MUR\": \"Mauritian Rupee\",\n" +
                                "    \"MVR\": \"Maldivian Rufiyaa\",\n" +
                                "    \"MWK\": \"Malawian Kwacha\",\n" +
                                "    \"MXN\": \"Mexican Peso\",\n" +
                                "    \"MYR\": \"Malaysian Ringgit\",\n" +
                                "    \"MZN\": \"Mozambican Metical\",\n" +
                                "    \"NAD\": \"Namibian Dollar\",\n" +
                                "    \"NGN\": \"Nigerian Naira\",\n" +
                                "    \"NIO\": \"Nicaraguan Córdoba\",\n" +
                                "    \"NOK\": \"Norwegian Krone\",\n" +
                                "    \"NPR\": \"Nepalese Rupee\",\n" +
                                "    \"NZD\": \"New Zealand Dollar\",\n" +
                                "    \"OMR\": \"Omani Rial\",\n" +
                                "    \"PAB\": \"Panamanian Balboa\",\n" +
                                "    \"PEN\": \"Peruvian Nuevo Sol\",\n" +
                                "    \"PGK\": \"Papua New Guinean Kina\",\n" +
                                "    \"PHP\": \"Philippine Peso\",\n" +
                                "    \"PKR\": \"Pakistani Rupee\",\n" +
                                "    \"PLN\": \"Polish Zloty\",\n" +
                                "    \"PYG\": \"Paraguayan Guarani\",\n" +
                                "    \"QAR\": \"Qatari Rial\",\n" +
                                "    \"RON\": \"Romanian Leu\",\n" +
                                "    \"RSD\": \"Serbian Dinar\",\n" +
                                "    \"RUB\": \"Russian Ruble\",\n" +
                                "    \"RWF\": \"Rwandan Franc\",\n" +
                                "    \"SAR\": \"Saudi Riyal\",\n" +
                                "    \"SBD\": \"Solomon Islands Dollar\",\n" +
                                "    \"SCR\": \"Seychellois Rupee\",\n" +
                                "    \"SDG\": \"Sudanese Pound\",\n" +
                                "    \"SEK\": \"Swedish Krona\",\n" +
                                "    \"SGD\": \"Singapore Dollar\",\n" +
                                "    \"SHP\": \"Saint Helena Pound\",\n" +
                                "    \"SLL\": \"Sierra Leonean Leone\",\n" +
                                "    \"SOS\": \"Somali Shilling\",\n" +
                                "    \"SRD\": \"Surinamese Dollar\",\n" +
                                "    \"SSP\": \"South Sudanese Pound\",\n" +
                                "    \"STD\": \"São Tomé and Príncipe Dobra (pre-2018)\",\n" +
                                "    \"STN\": \"São Tomé and Príncipe Dobra\",\n" +
                                "    \"SVC\": \"Salvadoran Colón\",\n" +
                                "    \"SYP\": \"Syrian Pound\",\n" +
                                "    \"SZL\": \"Swazi Lilangeni\",\n" +
                                "    \"THB\": \"Thai Baht\",\n" +
                                "    \"TJS\": \"Tajikistani Somoni\",\n" +
                                "    \"TMT\": \"Turkmenistani Manat\",\n" +
                                "    \"TND\": \"Tunisian Dinar\",\n" +
                                "    \"TOP\": \"Tongan Pa'anga\",\n" +
                                "    \"TRY\": \"Turkish Lira\",\n" +
                                "    \"TTD\": \"Trinidad and Tobago Dollar\",\n" +
                                "    \"TWD\": \"New Taiwan Dollar\",\n" +
                                "    \"TZS\": \"Tanzanian Shilling\",\n" +
                                "    \"UAH\": \"Ukrainian Hryvnia\",\n" +
                                "    \"UGX\": \"Ugandan Shilling\",\n" +
                                "    \"USD\": \"United States Dollar\",\n" +
                                "    \"UYU\": \"Uruguayan Peso\",\n" +
                                "    \"UZS\": \"Uzbekistan Som\",\n" +
                                "    \"VEF\": \"Venezuelan Bolívar Fuerte (Old)\",\n" +
                                "    \"VES\": \"Venezuelan Bolívar Soberano\",\n" +
                                "    \"VND\": \"Vietnamese Dong\",\n" +
                                "    \"VUV\": \"Vanuatu Vatu\",\n" +
                                "    \"WST\": \"Samoan Tala\",\n" +
                                "    \"XAF\": \"CFA Franc BEAC\",\n" +
                                "    \"XAG\": \"Silver Ounce\",\n" +
                                "    \"XAU\": \"Gold Ounce\",\n" +
                                "    \"XCD\": \"East Caribbean Dollar\",\n" +
                                "    \"XDR\": \"Special Drawing Rights\",\n" +
                                "    \"XOF\": \"CFA Franc BCEAO\",\n" +
                                "    \"XPD\": \"Palladium Ounce\",\n" +
                                "    \"XPF\": \"CFP Franc\",\n" +
                                "    \"XPT\": \"Platinum Ounce\",\n" +
                                "    \"YER\": \"Yemeni Rial\",\n" +
                                "    \"ZAR\": \"South African Rand\",\n" +
                                "    \"ZMW\": \"Zambian Kwacha\",\n" +
                                "    \"ZWL\": \"Zimbabwean Dollar\"\n" +
                                "}")
                ));

        /*
        Следующий мок возвращает json представление рандомной гифки с тегом поиска rich.
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
        Следующий мок возвращает json представление рандомной гифки с тегом поиска broke.
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
        Переданная валюта является пустой строкой.
         */
        mockMvc.perform(get("/images/gif")
                        .param("str", tag0))
                .andExpect(status().isNotFound());

        /*
        Переданная валюта является любой строкой, но не совпадает ни с одной валютой из списка валют.
         */
        mockMvc.perform(get("/images/gif")
                        .param("str", tag1))
                .andExpect(status().isNotFound());

        /*
        Курс переданной валюты за сегодня равен вчерашнему курсу.
         */
        mockMvc.perform(get("/images/gif")
                        .param("str", tag2))
                .andExpect(content().string(containsString(warnMessage)));




        /*
        Следующий мок возвращает json представление курса валюты RUB по отношению к USD за 2022-06-02.

        Согласно логике слоя сервиса, вместо переменной localDate в адрес этого мока будет приходить вчерашняя дата.
        Поэтому переменная localDate всегда будет содержать вчерашнюю дату.
        И этот мок всегда будет возвращать курса валюты RUB по отношению к USD за 2022-06-02.
         */
        stubFor(WireMock.get(urlPathEqualTo("/openexchangerates.org/api/historical/" + localDate + ".json"))
                .withId(UUID.randomUUID())
                .withQueryParam("app_id", equalTo(app_id))
                .withQueryParam("symbols", equalTo(tagOriginal))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                                "    \"license\": \"https://openexchangerates.org/license\",\n" +
                                "    \"timestamp\": 1654214372,\n" +
                                "    \"base\": \"USD\",\n" +
                                "    \"rates\": {\n" +
                                "        \"RUB\": 63.499993\n" +
                                "    }\n" +
                                "}")
                ));

        /*
        Переданная валюта является любой валютой из списка валют.
         */
        mockMvc.perform(get("/images/gif")
                        .param("str", tag2))
                .andExpect(status().isFound());
    }
}