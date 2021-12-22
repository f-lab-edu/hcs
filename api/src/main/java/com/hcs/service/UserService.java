package com.hcs.service;

import com.hcs.domain.User;
import com.hcs.dto.SignUpDto;
import com.hcs.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * @Service : 루트 컨테이너에 Bean 객체로 생성해주는 어노테이션. 서비스 레이어에 등록되며 자바 로직을 처리함.
 */

@Service
@RequiredArgsConstructor
public class UserService {

    //private final PasswordEncoder passwordEncoder; // security 설정 이후 코드 사용 예정
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;

    public User saveNewUser(@Valid SignUpDto signUpDto) {
        //signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword())); // security 설정 이후 코드 사용 예정
        User user = modelMapper.map(signUpDto, User.class);
        try {
            insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user;

    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public long insertUser(User user) {
        return userMapper.insertUser(user);
    }

    public long deleteUserByEmail(String email) {
        return userMapper.deleteUserByEmail(email);
    }

}
