package com.hcs.service;

import com.hcs.domain.User;
import com.hcs.dto.request.SignUpDto;
import com.hcs.dto.request.UserModifyDto;
import com.hcs.exception.global.DatabaseException;
import com.hcs.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * @Service : 루트 컨테이너에 Bean 객체로 생성해주는 어노테이션. 서비스 레이어에 등록되며 자바 로직을 처리함.
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final ModelMapper modelMapper;
    private final UserMapper userMapper;

    public User saveNewUser(SignUpDto signUpDto) {

        User user = modelMapper.map(signUpDto, User.class);

        long isSuccess = insertUser(user);

        if (isSuccess != 1) {
            throw new DatabaseException("DB user insert");
        }

        return user;
    }

    public User findById(long userId) {
        return userMapper.findById(userId);
    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public int countByEmail(String email) {
        return userMapper.countByEmail(email);
    }

    public int countByNickname(String nickname) {
        return userMapper.countByNickname(nickname);
    }

    public long insertUser(User user) {
        return userMapper.insertUser(user);
    }

    public long modifyUser(long userId, UserModifyDto userModifyDto) {

        User user = findById(userId);

        user = modelMapper.map(userModifyDto, User.class);
        user.setId(userId);

        int isSuccess = userMapper.updateUser(user);

        if (isSuccess != 1) {
            throw new DatabaseException("DB user modify");
        }

        return user.getId();
    }

    public long deleteUserById(long userId) {
        return userMapper.deleteUserById(userId);
    }
}
