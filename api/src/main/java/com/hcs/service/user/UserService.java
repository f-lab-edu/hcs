package com.hcs.service.user;

import com.hcs.domain.User;
import com.hcs.dto.SignUpDto;
import com.hcs.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * @Service : 루트 컨테이너에 Bean 객체로 생성해주는 어노테이션. 서비스 레이어에 등록되며 자바 로직을 처리함.
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
        try {
            save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user;

    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userMapper.existsByEmail(email);
    }

    public boolean existsByNickname(String nickname) {
        return userMapper.existsByNickname(nickname);
    }

    public void save(User user) {
        userMapper.save(user);
    }

    public void delete(String email) {
        userMapper.delete(email);
    }

}
