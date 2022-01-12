package com.hcs.mapper;

import com.hcs.domain.User;
import com.hcs.domain.UserForJPA;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Mapper : 매퍼 등록을 위한 애노테이션
 */

@Mapper
public interface UserMapper {

    User findById(long userId);

    User findByEmail(String email);

    long insertUser(User user);

    long deleteUserByEmail(String email);

    UserForJPA findUserForJpaByEmail(String email);
}
