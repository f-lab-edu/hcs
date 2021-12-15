package com.hcs.mapper;

import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest(includeFilters = {@ComponentScan.Filter(classes = {Configuration.class, Mapper.class, Bean.class})})
class UserMapperTest {

    User testUser = new User(); // Dummy 데이터

    @Autowired
    UserMapper userMapper;

    @DisplayName("UserMapper - 이메일로 User 찾기")
    @Test
    void findByEmailTest() {

        String realEmail = "test@naver.com";

        User returnedBy = userMapper.findByEmail(realEmail);

        assertThat(returnedBy.getClass()).isEqualTo(User.class);
        assertThat(returnedBy.getEmail()).isEqualTo(realEmail);
    }

    @DisplayName("UserMapper - parameter로 주어진 email의 User가 존재하는지의 여부 - 존재하는 경우")
    @Test
    void existsByEmailTest_correct_input() {

        String realEmail = "test@naver.com";

        boolean ifUserExists = userMapper.existsByEmail(realEmail);

        assertThat(ifUserExists).isTrue();
    }

    @DisplayName("UserMapper - parameter로 주어진 email의 User가 존재하는지의 여부 - 존재하지 않는 경우")
    @Test
    void existsByEmailTest_wrong_input() {

        String fakeEmail = "fake@naver.com";

        boolean ifUserExists = userMapper.existsByEmail(fakeEmail);

        assertThat(ifUserExists).isFalse();
    }

    @DisplayName("UserMapper - insert 테스트")
    @Test
    void insertUserTest() {

        String newEmail = "test2@naver.com";
        String newNickname = "test2";
        String newPassword = "password";
        settingTestUser(newEmail, newNickname, newPassword); // Dummy 데이터 셋팅 완료.

        long newUserId = userMapper.insertUser(testUser);

        assertThat(newUserId).isGreaterThan(0);
    }

    @DisplayName("UserMapper - delete 테스트")
    @Test
    void deleteUserByEmailTest() {

        String existingUserEmail = "test2@naver.com";

        long deletedUserId = userMapper.deleteUserByEmail(existingUserEmail);

        assertThat(deletedUserId).isGreaterThan(0);
    }

    public void settingTestUser(String email, String nickname, String password) {
        testUser.setEmail(email);
        testUser.setNickname(nickname);
        testUser.setPassword(password);
    }


}
