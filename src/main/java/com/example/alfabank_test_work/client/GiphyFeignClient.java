package com.example.alfabank_test_work.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.client.name.two}", url = "${giphy.url.general}")
public interface GiphyFeignClient {
    @GetMapping("/random")
    Object getRandomGif(@RequestParam String api_key, @RequestParam String tag);
}
