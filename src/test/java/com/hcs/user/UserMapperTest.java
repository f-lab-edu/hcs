package com.hcs.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.domain.User;
import com.hcs.dto.SignUpDto;
import com.hcs.mapper.UserMapper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@EnableEncryptableProperties
@Transactional
public class UserMapperTest {

    private static SignUpDto testSignUpDto = new SignUpDto();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("입력 읽어오기")
    @Test
    void mapperFindTest() throws Exception {
        //mysql db에 미리 [email : aaaa@naver.com, nick : test1, pass : password] 데이터를 넣어놓았음.
        User user = userMapper.findByEmail("aaaa@naver.com");
        assertNotNull(user);
        assertEquals(user.getPassword(), "12345678");
    }


    @DisplayName("입력 저장하기")
    @Test
    void mapperSaveTest() throws Exception {
        User user = User.builder().email("bbbb@naver.com").password("pass1234").nickname("bbbb").build();
        userMapper.save(user);
        User newUser = userMapper.findByEmail("bbbb@naver.com");
        assertNotNull(newUser);
        assertNotEquals(newUser.getPassword(), "12345678");
    }


}
