package com.hcs.user;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

/**
 * @SpringBootTest : 통합테스트를 목적으로 SpringBoot에서 제공하는 테스트 어노테이션
 * @AutoConfigureMockMvc : Mock 테스트 시 필요한 의존성을 제공해줌.
 * @EnableEncryptableProperties : application.yml 파일의 내용이 암호화된 경우, 복호화에 필요한 설정을 제공해줌.
 *
 * @Test : 테스트를 만드는 모듈 역할을 하는 어노테이션. 테스트 할 메소드를 지정하는데 사용됨.
 */

@SpringBootTest
@AutoConfigureMockMvc
@EnableEncryptableProperties
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원가입 화면 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("signUpForm"));

    }
}
