package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.common.JdbcTemplateHelper;
import com.hcs.common.TestSecurityConfig;
import com.hcs.domain.User;
import com.hcs.dto.request.SignUpDto;
import com.hcs.dto.request.UserModifyDto;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

@SpringBootTest(classes = TestSecurityConfig.class)
@EnableMockMvc
@EnableEncryptableProperties
@Transactional
public class UserControllerTest {

    private static SignUpDto testSignUpDto = new SignUpDto();
    private static UserModifyDto testModifyDto = new UserModifyDto();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;

    static Stream<Arguments> stringListProvider() {
        return Stream.of(
                arguments("nononono", "testtesttest", "12345678", Arrays.asList("email", "nickname", "password")),
                arguments("nononono", "test", "123123", Arrays.asList("email", "password")),
                arguments("test@naver.com", "te", "123123", Arrays.asList("nickname", "password")),
                arguments("nononono", "te", "12345678", Arrays.asList("email", "nickname"))
        );
    }

    static Stream<Arguments> stringListProvider2() {
        return Stream.of(
                arguments("noah0969@gmail.com", "nono2", "12345678", 99, "backend", "seoul", Arrays.asList("email")),
                arguments("noah09692@gmail.com", "nono", "12345678", 99, "backend", "seoul", Arrays.asList("nickname")),
                arguments("noah09692@gmail.com", "nono2", "12345678", 101, "backend", "seoul", Arrays.asList("age")),
                arguments("noah09692@gmail.com", "nono2", "12345678", 99, "backendbackend", "seoul", Arrays.asList("position")),
                arguments("noah09692@gmail.com", "nono2", "12345678", 99, "backend", "seoulseoulseoulseo", Arrays.asList("location")),
                arguments("noah0969@gmail.com", "nono", "12345678", 99, "backend", "seoul", Arrays.asList("email", "nickname")),
                arguments("noah09692@gmail.com", "nono", "12345678", 101, "backend", "seoul", Arrays.asList("nickname", "age")),
                arguments("noah0969@gmail.com", "nono", "12345678", 101, "backendbackend", "seoulseoulseoulseo", Arrays.asList("email", "nickname", "age", "position", "location"))
        );
    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @ParameterizedTest(name = "#{index} - {displayName} = Test with Argument={0}, {1}, {2}")
    @MethodSource("stringListProvider")
    void signUpSubmit_with_wrong_input(String email, String nickname, String password, List<String> invalidFields) throws Exception {

        testSignUpDto.setEmail(email);
        testSignUpDto.setNickname(nickname);
        testSignUpDto.setPassword(password);

        MvcResult mvcResult = mockMvc.perform(post("/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignUpDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int length = JsonPath.parse(response).read("$.HCS.item.errors.length()");

        for (int i = 0; i < length; i++) {
            String field = JsonPath.parse(response).read("$.HCS.item.errors[" + i + "].field");
            assertThat(invalidFields).contains(field);
        }
    }

    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {

        testSignUpDto.setNickname("noah1");
        testSignUpDto.setEmail("noah0969@gmail.com");
        testSignUpDto.setPassword("12345678");

        MvcResult mvcResult = mockMvc.perform(post("/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignUpDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        User user = userMapper.findByEmail("noah0969@gmail.com");

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("userId")).isEqualTo((int) user.getId());
    }

    @DisplayName("사용자 정보 요청시 리턴되는 body를 확인")
    @Test
    void userInfo_with_correct_req() throws Exception {
        User newUser = User.builder()
                .nickname("noah0504")
                .email("test@naver.com")
                .password("12345678")
                .location("test loc")
                .age(10)
                .emailCheckToken("token")
                .emailCheckTokenGeneratedAt(LocalDateTime.now())
                .emailVerified(true)
                .position("test pos")
                .joinedAt(LocalDateTime.now())
                .build();
        userMapper.insertUser(newUser);
        User user = userMapper.findByEmail(newUser.getEmail());

        MvcResult mvcResult = mockMvc.perform(get("/user/")
                        .param("userId", String.valueOf(user.getId())))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);

        HashMap<String, Object> profile = (HashMap<String, Object>) item.get("profile");

        assertThat(profile.get("userId")).isEqualTo((int) user.getId());
        assertThat(profile.get("email")).isEqualTo(user.getEmail());
        assertThat(profile.get("nickname")).isEqualTo(user.getNickname());
        //  assertThat(profile.get("emailVerified")).isEqualTo(user.getEmailVerified());
        assertThat(profile.get("joinedAt")).isEqualTo(user.getJoinedAt().toString());
        assertThat(profile.get("age")).isEqualTo(user.getAge());
        assertThat(profile.get("position")).isEqualTo(user.getPosition());
        assertThat(profile.get("location")).isEqualTo(user.getLocation());
    }

    @DisplayName("회원 정보 수정 - 입력값 정상")
    @Test
    void modifyUser_with_correct_input() throws Exception {

        User newUser = User.builder()
                .email("noah0969@gmail.com")
                .nickname("nono")
                .password("123456789")
                .build();

        userMapper.insertUser(newUser);
        long userId = newUser.getId();

        testModifyDto.setEmail("noah09691@gmail.com");
        testModifyDto.setNickname("test123");
        testModifyDto.setPassword("12345678");
        testModifyDto.setAge(25);
        testModifyDto.setPosition("backend");
        testModifyDto.setLocation("Seoul");

        MvcResult mvcResult = mockMvc.perform(put("/user/")
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testModifyDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        int modifiedUserId = JsonPath.parse(response).read("$.HCS.item.userId");

        assertThat(status).isEqualTo(200);
        assertThat(modifiedUserId).isEqualTo(userId);

        User modifiedUser = userMapper.findById(userId);

        assertThat(modifiedUser.getEmail()).isEqualTo("noah09691@gmail.com");
        assertThat(modifiedUser.getNickname()).isEqualTo("test123");
        assertThat(modifiedUser.getPassword()).isEqualTo("12345678");
        assertThat(modifiedUser.getAge()).isEqualTo(25);
        assertThat(modifiedUser.getPosition()).isEqualTo("backend");
        assertThat(modifiedUser.getLocation()).isEqualTo("Seoul");
    }

    @DisplayName("회원 정보 수정 - 입력값 오류")
    @ParameterizedTest(name = "#{index} - {displayName} = Test with Argument={0}, {1}, {2}, {3}, {4}, {5}")
    @MethodSource("stringListProvider2")
    void modifyUser_with_wrong_input(String email, String nickname, String password, int age, String position, String location, List<String> invalidFields) throws Exception {

        User newUser1 = User.builder()
                .email("noah0969@gmail.com")
                .nickname("nono")
                .password("12345678")
                .build();

        User newUser2 = User.builder()
                .email("noah09691@gmail.com")
                .nickname("nono1")
                .password("12345678")
                .build();

        userMapper.insertUser(newUser1);
        userMapper.insertUser(newUser2);
        long userId = newUser2.getId();

        testModifyDto.setEmail(email);
        testModifyDto.setNickname(nickname);
        testModifyDto.setPassword(password);
        testModifyDto.setAge(age);
        testModifyDto.setPosition(position);
        testModifyDto.setLocation(location);

        MvcResult mvcResult = mockMvc.perform(put("/user/")
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testModifyDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        int length = JsonPath.parse(response).read("$.HCS.item.errors.length()");

        assertThat(status).isEqualTo(400);

        for (int i = 0; i < length; i++) {
            String field = JsonPath.parse(response).read("$.HCS.item.errors[" + i + "].field");
            assertThat(invalidFields).contains(field);
        }
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteUserTest() throws Exception {

        User newUser1 = User.builder()
                .email("noah0969@gmail.com")
                .nickname("nono")
                .password("12345678")
                .build();

        userMapper.insertUser(newUser1);

        long userId = newUser1.getId();

        MvcResult mvcResult = mockMvc.perform(delete("/user/")
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSignUpDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        int count = userMapper.countByEmail("noah0969@gmail.com");

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(count).isEqualTo(0);
        assertThat(status).isEqualTo(200);
        assertThat(item.get("userId")).isEqualTo((int) userId);
    }
}
