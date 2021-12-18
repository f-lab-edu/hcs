package com.hcs.mapper;

import com.hcs.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Mapper : 매퍼 등록을 위한 애노테이션
 */

@Mapper
public interface UserMapper {

    User findByEmail(String email);

    long insertUser(User user);

    long deleteUserByEmail(String email);
}
