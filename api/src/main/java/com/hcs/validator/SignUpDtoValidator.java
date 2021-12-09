package com.hcs.validator;

import com.hcs.dto.SignUpDto;
import com.hcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpDtoValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpDto.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpDto signUpDto = (SignUpDto) object;

        if (userService.existsByEmail(signUpDto.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpDto.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if (userService.existsByNickname(signUpDto.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpDto.getEmail()}, "이미 사용중인 닉네임입니다.");
        }

    }
}
