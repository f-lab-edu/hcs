package com.hcs.mapper;

import com.hcs.domain.Club;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Param : mybatis 에서 sql 문장에 다수의 파라미터를 전달할 때 변수이름을 각각 지정해주기위해 사용한다.
 */

@Mapper
public interface ClubMapper {

    Club findById(Long id);

    Club findByTitle(String title);

    void insertClub(Club club);

    void deleteClubById(Long id);

    Club findClubWithMembers(Long id);

    Club findClubWithManagers(Long id);

    void joinMemberById(@Param("clubId") Long clubId, @Param("memberId") Long userId);

    void joinManagerById(@Param("clubId") Long clubId, @Param("managerId") Long userId);

}
