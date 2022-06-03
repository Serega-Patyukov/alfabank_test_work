package com.example.alfabank_test_work.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AlfaTestWorkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getGifTest() throws Exception {

        mockMvc.perform(get("/images/gif")
                        .param("str", ""))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/images/gif")
                        .param("str", "ru"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/images/gif")
                        .param("str", "rub"))
                .andExpect(status().isFound());
    }
}
