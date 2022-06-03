package com.example.alfabank_test_work;

import com.example.alfabank_test_work.controller.AlfaTestWorkController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AlfabankTestWorkApplicationTests {

    @Autowired
    private AlfaTestWorkController alfaTestWorkController;

    @Test
    void contextLoads() {
        assertThat(alfaTestWorkController).isNotNull();
    }

}
