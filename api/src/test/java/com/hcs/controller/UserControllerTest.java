package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.config.EnableMockMvc;
import com.hcs.domain.User;
import com.hcs.dto.SignUpDto;
import com.hcs.mapper.UserMapper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @SpringBootTest : 통합테스트를 목적으로 SpringBoot에서 제공하는 테스트 어노테이션
 * @EnableMockMvc : MockMvc 한글 깨짐 현상을 해결하기 위해 @AutoConfigureMockMvc에 주입시킬 필터가 추가된 어노테이션.
 * @EnableEncryptableProperties : application.yml 파일의 내용이 암호화된 경우, 복호화에 필요한 설정을 제공해줌.
 * @Transactional : 적용된 범위에서 트랜잭션 기능이 포함된 프록시 객체가 생성되어 자동으로 commit or rollback을 진행해준다.
 * @Test : 테스트를 만드는 모듈 역할을 하는 어노테이션. 테스트 할 메소드를 지정하는데 사용됨.
 */

@SpringBootTest
@EnableMockMvc
@EnableEncryptableProperties
@Transactional
public class UserControllerTest {

    private static SignUpDto testSignUpDto = new SignUpDto();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("회원가입 화면 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("signUpForm"));

    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception {

        testSignUpDto.setNickname("no");
        testSignUpDto.setEmail("nononono");
        testSignUpDto.setPassword("123123");

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignUpDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))

                .andDo(print())
                .andExpect(status().isOk());


        userMapper.delete(testSignUpDto.getEmail());

    }

    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {

        testSignUpDto.setNickname("noah");
        testSignUpDto.setEmail("noah0504@naver.com");
        testSignUpDto.setPassword("12345678");

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignUpDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))

                .andDo(print())
                .andExpect(status().is3xxRedirection());


        User user = userMapper.findByEmail("noah0504@naver.com");
        assertNotNull(user);
        assertNotEquals(user.getPassword(), "12345678");

        userMapper.delete(testSignUpDto.getEmail());

    }

}
