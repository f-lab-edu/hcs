package com.hcs.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.controller.test.TestUserController;
import com.hcs.controller.test.TestUserControllerWithoutValid;
import com.hcs.dto.request.SignUpDto;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Validation 테스트를 위하여 생성된 클래스.
 *
 * @SpringBootTest : 통합테스트를 목적으로 SpringBoot에서 제공하는 테스트 어노테이션.
 * @EnableMockMvc : MockMvc 한글 깨짐 현상을 해결하기 위해 @AutoConfigureMockMvc에 주입시킬 필터가 추가된 어노테이션.
 * @EnableEncryptableProperties : application.yml 파일의 내용이 암호화된 경우, 복호화에 필요한 설정을 제공해줌.
 * @Transactional : 적용된 범위에서 트랜잭션 기능이 포함된 프록시 객체가 생성되어 자동으로 commit or rollback을 진행해준다.
 * @Test : 테스트를 만드는 모듈 역할을 하는 어노테이션. 테스트 할 메소드를 지정하는데 사용됨.
 */

@SpringBootTest
@EnableMockMvc
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
                        .accept(MediaType.APPLICATION_JSON))
                //.with(csrf())) // security 설정 이후 코드 사용 예정

                .andDo(print())
                .andExpect(handler().handlerType(TestUserController.class))
                .andExpect(status().isOk());

    }

    @DisplayName("회원 가입 처리 - 입력값 오류 - @Valid 적용 안함")
    @Test
    void signUpSubmit_with_wrong_input_no_Valid() throws Exception {

        testSignUpDto.setNickname("no");
        testSignUpDto.setEmail("nononono");
        testSignUpDto.setPassword("123123");

        mockMvc.perform(post("/test/valid/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignUpDto))
                        .accept(MediaType.APPLICATION_JSON))
                //.with(csrf())) // security 설정 이후 코드 사용 예정

                .andDo(print())
                .andExpect(handler().handlerType(TestUserControllerWithoutValid.class))
                .andExpect(status().is3xxRedirection());

    }

}
