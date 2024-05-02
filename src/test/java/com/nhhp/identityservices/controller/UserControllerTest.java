package com.nhhp.identityservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhhp.identityservices.dto.request.UserCreationRequest;
import com.nhhp.identityservices.dto.response.UserResponse;
import com.nhhp.identityservices.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.time.LocalDate;

// pham vi unit test: chi test trong class, neu trong class su dung ham cua class khac, ta can mock ham do, dung @mockbean
// vd: trong class cua Controller, khi test create, no co goi ham userService.createUser() tu class Service, ta can dung
//     @mockbean
//     private UserService userService;
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc // tao request den controller khi test
@TestPropertySource("/test.properties") // khi co annotation nay, thay vi doc config trong application.yaml, no se doc
// config trong file test.properties
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // bat ky test nao cung can input - output
    private UserCreationRequest request; // tao input

    private UserResponse userResponse; // tao output

    @BeforeEach // khoi tao chung truoc khi cac test cua @Test duoc chay
    void initData(){
        var dob = LocalDate.of(1990, 1, 1);

        // mong muon tao request voi thong so nhu vay
        request = UserCreationRequest.builder()
                .username("john")
                .firstName("john")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();


        // thi dau ra mong muon nhu vay
        userResponse = UserResponse.builder()
                .id("fff4bbb86385")
                .username("john")
                .firstName("john")
                .lastName("Doe")
                .dob(dob)
                .build();

    }

    @Test // tao unittest, pham vi unit test: chi test trong class
    void createUser_validRequest_success() throws Exception {
        // test gom 3 phan
        // GIVEN: nhung dau vao da tao, du doan se xay ra nhu vay
        // WHEN: test khi nao, vd khi request
        // THEN: khi quen xay ra thi ta mong muon response la gi

        // GIVEN: da tao o tren
        // chuyen request sang String dung ObjectMapper, do yeu cau cua .content()
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // theo nguyen tac, ta can moc method thuoc class khac khi dang test o class hien tai
        // when(userService.createUser(ArgumentMatchers.any())): no se khong goi method createUser cua UserService
        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)

                )       .andExpect(MockMvcResultMatchers.status().isOk()) // mong muon status output la 200: ok
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id")
                .value("fff4bbb86385")); // mong muon output co gia tri: code:0 trong chuoi json
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN
        request.setUsername("john");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1005))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Username must be at least 4 characters")
                );
    }
}
