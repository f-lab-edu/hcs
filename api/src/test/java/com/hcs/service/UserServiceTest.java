package com.hcs.service;

import com.hcs.domain.User;
import com.hcs.dto.request.SignUpDto;
import com.hcs.dto.request.UserModifyDto;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @ExtendWith : 테스트 클래스가 Mockito를 사용함을 의미함.
 * @Mock : mock 객체를 생성함.
 * @InjectMocks : 생성한 Mock객체를 주입하여 사용할 수 있도록 만든 객체.
 * @BeforeEach : 테스트 케이스 시작 전에 먼저 실행되는 어노테이션.
 */

@SpringBootTest
@EnableEncryptableProperties
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("User 추가가 제대로 동작하는지 테스트")
    void testSaveNewUser() {

        // given
        String email = "noah0504@naver.com";
        String nickname = "noah";
        String password = "12345678";

        // when
        SignUpDto signUpDto = SignUpDto.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();

        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();

        User newUser = userService.saveNewUser(signUpDto);
        user.setId(newUser.getId());

        // then
        assertThat(newUser).isEqualTo(user);
    }

    @Test
    @DisplayName("User 정보 수정이 제대로 동작하는지 테스트")
    void modifyUserTest() {

        // given
        String newEmail = "test3@naver.com";
        String newNickname = "test";
        String newPassword = "password";

        User fixtureUser = User.builder()
                .email(newEmail)
                .nickname(newNickname)
                .password(newPassword)
                .build();

        userService.insertUser(fixtureUser);

        int age = 20;
        String position = "backend";
        String location = "Seoul";

        long userId = fixtureUser.getId();

        UserModifyDto modifyDto = UserModifyDto.userModifyDtoBuilder()
                .email(newEmail + 1)
                .nickname(newNickname + 1)
                .password(newPassword + 1)
                .age(age)
                .position(position)
                .location(location)
                .build();

        // when
        long modifiedUserId = userService.modifyUser(userId, modifyDto);
        Optional<User> modified = Optional.ofNullable(userService.findById(userId));

        // that
        assertThat(modifiedUserId).isEqualTo(userId);
        assertThat(modified.get().getEmail()).isEqualTo(newEmail + 1);
        assertThat(modified.get().getNickname()).isEqualTo(newNickname + 1);
        assertThat(modified.get().getPassword()).isEqualTo(newPassword + 1);
        assertThat(modified.get().getAge()).isEqualTo(age);
        assertThat(modified.get().getPosition()).isEqualTo(position);
        assertThat(modified.get().getLocation()).isEqualTo(location);
    }
}
