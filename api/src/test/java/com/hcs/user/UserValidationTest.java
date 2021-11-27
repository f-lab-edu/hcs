package com.hcs.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.controller.user.test.TestUserController;
import com.hcs.dto.SignUpDto;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableEncryptableProperties
@Transactional
public class UserValidationTest {

    private static SignUpDto testSignUpDto = new SignUpDto();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("회원 가입 처리 - 입력값 오류 - @InitBinder 적용 안함")
    @Test
    void signUpSubmit_with_wrong_input_no_initBinder() throws Exception {

        testSignUpDto.setNickname("no");
        testSignUpDto.setEmail("nononono");
        testSignUpDto.setPassword("123123");

        mockMvc.perform(post("/test/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignUpDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))

                .andDo(print())
                .andExpect(handler().handlerType(TestUserController.class))
                .andExpect(status().isOk());

    }

}
