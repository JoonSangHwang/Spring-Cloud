package com.example.userservice.controller;

import com.example.userservice.user.dto.RequestUser;
import com.example.userservice.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @DisplayName("유저 생성 기능 - [정상] 테스트")
    public void createUser() throws Exception {
        RequestUser userDto = RequestUser.builder()
                .name("Hwang Jun Sang")
                .email("gufrus@naver.com")
                .pwd("test1234")
                .build();

        mockMvc.perform(post("/user-service/users")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)    // Header의 Content-Type
                    .accept(MediaTypes.HAL_JSON_VALUE)                // 요구 Content-Type
                    .content(objectMapper.writeValueAsString(userDto))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("유저 전체 조회 - [정상] 테스트")
    public void getUsers() throws Exception {
        mockMvc.perform(get("/user-service/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)    // Header의 Content-Type
                .accept(MediaTypes.HAL_JSON_VALUE)                // 요구 Content-Type
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}