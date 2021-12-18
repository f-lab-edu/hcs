package com.hcs.mapper;

import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[MyBatisConfig]")})
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    void insertTestUser(String newEmail, String newNickname, String newPassword) {

        String insertSql = "insert into User (email, nickname, password)\n" +
                "values (?, ?, ?)";

        jdbcTemplate.update(insertSql, new Object[]{newEmail, newNickname, newPassword});
    }

    @DisplayName("UserMapper - 이메일로 User 찾기")
    @Test
    void findByEmailTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        insertTestUser(newEmail, newNickname, newPassword);

        Optional<User> returnedBy = Optional.ofNullable(userMapper.findByEmail(newEmail));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getEmail()).isEqualTo(newEmail);
        assertThat(returnedBy.get().getNickname()).isEqualTo(newNickname);
        assertThat(returnedBy.get().getPassword()).isEqualTo(newPassword);

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

        User testUser = User.builder()
                .email(newEmail)
                .nickname(newNickname)
                .password(newPassword)
                .build();

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
}
