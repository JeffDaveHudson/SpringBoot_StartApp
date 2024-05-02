package com.nhhp.identityservices.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhhp.identityservices.dto.request.UserCreationRequest;
import com.nhhp.identityservices.dto.response.UserResponse;
import com.nhhp.identityservices.entity.User;
import com.nhhp.identityservices.exception.AppException;
import com.nhhp.identityservices.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    // bat ky test nao cung can input - output
    private UserCreationRequest request; // tao input

    private UserResponse userResponse; // tao output

    private User user;

    @BeforeEach
        // khoi tao chung truoc khi cac test cua @Test duoc chay
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

        user = User.builder()
                .id("fff4bbb86385")
                .username("john")
                .firstName("john")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
        // tao unittest, pham vi unit test: chi test trong class
    void createUser_validRequest_success() throws Exception{
        //GIVEN
        Mockito.when(userRepository.existsByUsername(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(user);

        //WHEN
        var response = userService.createUser(request);

        //THEN
        Assertions.assertThat(response.getId()).isEqualTo("fff4bbb86385");
        Assertions.assertThat(response.getUsername()).isEqualTo("john");
    }

    @Test
        //
    void createUser_userExisted_fail() throws Exception {
        //GIVEN
        Mockito.when(userRepository.existsByUsername(Mockito.anyString()))
                .thenReturn(true);

        //WHEN
        var response = userService.createUser(request);

        //THEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        Assertions.assertThat(response.getUsername()).isEqualTo("john");

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1234);
    }

}
