package com.hcs.mapper;

import com.hcs.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Mapper : 매퍼 등록을 위한 애노테이션
 */

@Mapper
public interface UserMapper {

    User findById(long userId);

    User findByEmail(String email);

    User findByNickname(String nickname);

    int countByEmail(String email);

    int countByNickname(String nickname);

    long insertUser(User user);

    int updateUser(User user);

    long deleteUserById(long userId);
}
