package com.hcs.mapper;

import com.hcs.domain.Club;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClubMapper {

    Club findById(Long id);

    Club findByTitle(String title);

    void save(Club club);

    void delete(Long id);
}
