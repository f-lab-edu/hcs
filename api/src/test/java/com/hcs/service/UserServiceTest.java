package com.hcs.service;

import com.hcs.domain.User;
import com.hcs.dto.SignUpDto;
import com.hcs.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @ExtendWith : 테스트 클래스가 Mockito를 사용함을 의미함.
 * @Mock : mock 객체를 생성함.
 * @InjectMocks : 생성한 Mock객체를 주입하여 사용할 수 있도록 만든 객체.
 * @BeforeEach : 테스트 케이스 시작 전에 먼저 실행되는 어노테이션.
 */

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserMapper userMapper;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void setUp() {
        modelMapper = new ModelMapper();
        userService = new UserService(modelMapper, userMapper);
    }

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

        // then
        assertThat(userService.saveNewUser(signUpDto)).isEqualTo(user);
    }
}
