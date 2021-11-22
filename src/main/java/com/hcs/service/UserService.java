package com.hcs.service;

import com.hcs.domain.User;
import com.hcs.dto.SignUpDto;
import com.hcs.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Service
 *      : 루트 컨테이너에 Bean 객체로 생성해주는 어노테이션. 서비스 레이어에 등록되며 자바 로직을 처리함.
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;


    public User saveNewUser(@Valid SignUpDto signUpDto) {
        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        User user = modelMapper.map(signUpDto, User.class);
        return user;
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

}
