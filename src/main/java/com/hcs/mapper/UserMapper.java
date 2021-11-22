package com.hcs.mapper;

import com.hcs.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Mapper : 매퍼 등록을 위한 애노테이션
 */

@Mapper
public interface UserMapper {

    List<User> findAll();
}
