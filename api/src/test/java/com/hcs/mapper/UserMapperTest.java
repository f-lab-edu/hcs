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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[DataSourceConfig]")
        , @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[Hcs].*")})
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

    @DisplayName("UserMapper - 사용자 Id(DB의 Prmiary Key)로 User 찾기")
    @Test
    void findByIdTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        insertTestUser(newEmail, newNickname, newPassword);

        Optional<User> insertedUser = Optional.ofNullable(userMapper.findByEmail(newEmail));
        Optional<User> returnedBy = Optional.ofNullable(userMapper.findById(insertedUser.get().getId()));

        assertThat(returnedBy).isNotEmpty();
        assertThat(returnedBy.get().getEmail()).isEqualTo(newEmail);
        assertThat(returnedBy.get().getNickname()).isEqualTo(newNickname);
        assertThat(returnedBy.get().getPassword()).isEqualTo(newPassword);
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
