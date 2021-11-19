package com.hcs.service;

import com.hcs.domain.User;
import com.hcs.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * @Service :
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public User saveNewUser(@Valid SignUpDto signUpDto) {
        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        User user = modelMapper.map(signUpDto, User.class);
        return user;
    }
}
