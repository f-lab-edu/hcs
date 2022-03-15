package com.hcs.service;

import com.hcs.domain.User;
import com.hcs.dto.request.SignUpDto;
import com.hcs.dto.request.UserModifyDto;
import com.hcs.mapper.UserMapper;
import com.hcs.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Service : 루트 컨테이너에 Bean 객체로 생성해주는 어노테이션. 서비스 레이어에 등록되며 자바 로직을 처리함.
 */

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final ModelMapper modelMapper;
    private final UserMapper userMapper;

    public User saveNewUser(SignUpDto signUpDto) {

        User user = modelMapper.map(signUpDto, User.class);
        user.setJoinedAt(LocalDateTime.now());

        insertUser(user);

        return user;
    }

    public User saveNewOAuthUser(User user) {
        user.setJoinedAt(LocalDateTime.now());
        insertUser(user);

        return user;
    }

    public User findById(long userId) {
        return userMapper.findById(userId);
    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = findByEmail(username);

        if (user == null) {
            System.out.println("email " + username);
            System.out.println("not existed email");
            throw new UsernameNotFoundException("해당 email의 사용자가 존재하지 않습니다.");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER")); // TODO : user domain의 role field를 값으로 주입

        UserContext userContext = new UserContext(user, roles);

        return userContext;
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

        userMapper.updateUser(user);

        return user.getId();
    }

    public long deleteUserById(long userId) {
        return userMapper.deleteUserById(userId);
    }
}
