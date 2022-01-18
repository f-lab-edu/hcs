package com.hcs.mapper;

import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = {".*DataSourceConfig", ".*JasyptConfig"})})
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    long insertTestUser(String newEmail, String newNickname, String newPassword) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String insertSql = "insert into User (email, nickname, password)\n" +
                "values (?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newEmail);
            ps.setString(2, newNickname);
            ps.setString(3, newPassword);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @DisplayName("UserMapper - 사용자 Id(DB의 Prmiary Key)로 User 찾기")
    @Test
    void findByIdTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        long userId = insertTestUser(newEmail, newNickname, newPassword);

        Optional<User> returnedBy = Optional.ofNullable(userMapper.findById(userId));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getEmail()).isEqualTo(newEmail);
        assertThat(returnedBy.get().getNickname()).isEqualTo(newNickname);
        assertThat(returnedBy.get().getPassword()).isEqualTo(newPassword);
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

    @DisplayName("UserMapper - 닉네임으로 User 찾기")
    @Test
    void findByNicknameTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        insertTestUser(newEmail, newNickname, newPassword);

        Optional<User> returnedBy = Optional.ofNullable(userMapper.findByNickname(newNickname));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getEmail()).isEqualTo(newEmail);
        assertThat(returnedBy.get().getNickname()).isEqualTo(newNickname);
        assertThat(returnedBy.get().getPassword()).isEqualTo(newPassword);
    }

    @DisplayName("UserMapper - 이메일로 User 수 찾기")
    @Test
    void countByEmailTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        insertTestUser(newEmail, newNickname, newPassword);
        insertTestUser(newEmail, newNickname + 1, newPassword + 1);
        insertTestUser(newEmail, newNickname + 2, newPassword + 2);

        int count = userMapper.countByEmail(newEmail);

        assertThat(count).isEqualTo(3);
    }

    @DisplayName("UserMapper - 닉네임으로 User 수 찾기")
    @Test
    void countByNicknameTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        insertTestUser(newEmail, newNickname, newPassword);
        insertTestUser(newEmail + 1, newNickname, newPassword + 1);
        insertTestUser(newEmail + 2, newNickname, newPassword + 2);

        int count = userMapper.countByNickname(newNickname);

        assertThat(count).isEqualTo(3);
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

        userMapper.insertUser(testUser);

        assertThat(testUser.getId()).isGreaterThan(0);
    }

    @DisplayName("UserMapper - update 테스트")
    @Test
    void updateUserTest() {

        String newEmail = "test2@naver.com";
        String newNickname = "test2";
        String newPassword = "password";

        long userId = insertTestUser(newEmail, newNickname, newPassword);

        int age = 20;
        String position = "backend";
        String location = "Seoul";

        User modifiedUser = User.builder()
                .id(userId)
                .email(newEmail + 1)
                .nickname(newNickname + 1)
                .password(newPassword + 1)
                .age(age)
                .position(position)
                .location(location)
                .build();

        int isSuccess = userMapper.updateUser(modifiedUser);

        assertThat(isSuccess).isGreaterThan(0);
    }

    @DisplayName("UserMapper - delete 테스트")
    @Test
    void deleteUserByIdTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        long userId = insertTestUser(newEmail, newNickname, newPassword);
        long isSuccess = userMapper.deleteUserById(userId);

        assertThat(isSuccess).isGreaterThan(0);
    }
}
