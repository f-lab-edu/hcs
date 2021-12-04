package com.hcs.mapper.user;

import com.hcs.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Mapper : 매퍼 등록을 위한 애노테이션
 */

@Mapper
public interface UserMapper {

    User findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    void save(User user);

    void delete(String email);
}
