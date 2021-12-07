package com.hcs.mapper.club;

import com.hcs.domain.Club;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ClubMapper {

    Club findByTitle(String title);

    Club findById(Long id);

    void save(Club club);

    void delete(Long id);

    Club findClubWithMembers(Long id);

    Club findClubWithManagers(Long id);

    void joinMemberById(@Param("clubId") Long clubId, @Param("memberId") Long userId);

    void joinManagerById(@Param("clubId") Long clubId, @Param("managerId") Long userId);

}
