package com.example.alfabank_test_work.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "GiphyFeignClient", url = "https://api.giphy.com/v1/gifs")
public interface GiphyFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/random?api_key={api_key}&tag={rich}")
    Object getRich(@PathVariable String api_key, @PathVariable String rich);

    @RequestMapping(method = RequestMethod.GET, value = "/random?api_key={api_key}&tag={broke}")
    Object getBroke(@PathVariable String api_key, @PathVariable String broke);
}
