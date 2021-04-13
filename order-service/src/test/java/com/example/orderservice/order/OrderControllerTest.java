package com.example.orderservice.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Test
//    @DisplayName("유저 생성 기능 - [정상] 테스트")
    @DisplayName("Order - [Health Check]")
    public void orderHealthCheck() throws Exception {
        mockMvc.perform(get("/order-service/health_check")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)    // Header의 Content-Type
                    .accept(MediaTypes.HAL_JSON_VALUE)                // 요구 Content-Type
                )
                .andDo(print())
                .andExpect(status().isOk());


    }


}