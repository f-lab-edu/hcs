package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.domain.User;
import com.hcs.dto.request.SignUpDto;
import com.hcs.mapper.UserMapper;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @SpringBootTest : 통합테스트를 목적으로 SpringBoot에서 제공하는 테스트 어노테이션
 * @EnableMockMvc : MockMvc 한글 깨짐 현상을 해결하기 위해 @AutoConfigureMockMvc에 주입시킬 필터가 추가된 어노테이션.
 * @EnableEncryptableProperties : application.yml 파일의 내용이 암호화된 경우, 복호화에 필요한 설정을 제공해줌.
 * @Transactional : 적용된 범위에서 트랜잭션 기능이 포함된 프록시 객체가 생성되어 자동으로 commit or rollback을 진행해준다.
 * @Test : 테스트를 만드는 모듈 역할을 하는 어노테이션. 테스트 할 메소드를 지정하는데 사용됨.
 * @ParameterizedTest : 여러 argument로 테스트를 여러번 돌릴 수 있는 어노테이션
 * @MethodSource : factory method가 리턴해주는 값이 parameter로 쓰이도록 주입해주는 어노테이션
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

    static Stream<Arguments> stringListProvider() {
        return Stream.of(
                arguments("nononono", "no", "123123", Arrays.asList("nickname", "email", "password")),
                arguments("nononono", "noah", "123123", Arrays.asList("email", "password")),
                arguments("noah0504@naver.com", "no", "123123", Arrays.asList("nickname", "password")),
                arguments("nononono", "no", "12345678", Arrays.asList("nickname", "email"))
        );
    }

    @DisplayName("회원가입 화면 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @ParameterizedTest(name = "#{index} - {displayName} = Test with Argument={0}, {1}, {2}")
    @MethodSource("stringListProvider")
    void signUpSubmit_with_wrong_input(String email, String nickname, String password, List<String> invalidFields) throws Exception {

        testSignUpDto.setEmail(email);
        testSignUpDto.setNickname(nickname);
        testSignUpDto.setPassword(password);

        MvcResult mvcResult = mockMvc.perform(post("/user/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignUpDto))
                        .accept(MediaType.APPLICATION_JSON))
                //.with(csrf())) // security 설정 이후 코드 사용 예정

                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int length = JsonPath.parse(response).read("$.errors.length()");

        for (int i = 0; i < length; i++) {
            String field = JsonPath.parse(response).read("$.errors[" + i + "].field");
            assertThat(invalidFields).contains(field);
        }
    }

    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {

        testSignUpDto.setNickname("noah");
        testSignUpDto.setEmail("noah0969@gmail.com");
        testSignUpDto.setPassword("12345678");

        MvcResult mvcResult = mockMvc.perform(post("/user/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignUpDto))
                        .accept(MediaType.APPLICATION_JSON))
                //.with(csrf())) // security 설정 이후 코드 사용 예정

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User user = userMapper.findByEmail("noah0969@gmail.com");

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("userId")).isEqualTo(user.getId().intValue());

    }

    @DisplayName("사용자 정보 요청시 리턴되는 body를 확인")
    @Test
    void userInfo_with_correct_req() throws Exception {
        // TODO (테스트를 위해) 사용자 추가

        User user = userMapper.findByEmail("noah0504@naver.com");

        MvcResult mvcResult = mockMvc.perform(get("/user/info")
                        .param("userId", "31"))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // TODO mvcResult의 Body를 테스트

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("userId")).isEqualTo(user.getId().intValue());

        HashMap<String, Object> profile = (HashMap<String, Object>) item.get("profile");

        assertThat(profile.get("email")).isEqualTo(user.getEmail());
        assertThat(profile.get("nickname")).isEqualTo(user.getNickname());
        assertThat(profile.get("emailVerified")).isEqualTo(user.isEmailVerified());
        assertThat(profile.get("joinedAt")).isEqualTo(user.getJoinedAt().toString());
        assertThat(profile.get("age")).isEqualTo(user.getAge());
        assertThat(profile.get("position")).isEqualTo(user.getPosition());
        assertThat(profile.get("location")).isEqualTo(user.getLocation());
    }
}
