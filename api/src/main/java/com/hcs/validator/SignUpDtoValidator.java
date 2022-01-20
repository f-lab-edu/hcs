package com.hcs.validator;

import com.hcs.domain.User;
import com.hcs.dto.request.SignUpDto;
import com.hcs.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SignUpDtoValidator implements Validator {

    private final UserMapper userMapper;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpDto signUpDto = (SignUpDto) object;

        User user = userMapper.findByEmail(signUpDto.getEmail());
        Optional<User> userOptional = Optional.ofNullable(user);

        User user2 = userMapper.findByNickname(signUpDto.getNickname());
        Optional<User> userOptional2 = Optional.ofNullable(user2);

        if (userOptional.isPresent()) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpDto.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if (userOptional2.isPresent()) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpDto.getNickname()}, "이미 사용중인 닉네임입니다.");
        }
    }
}
