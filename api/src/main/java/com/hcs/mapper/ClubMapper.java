package com.hcs.mapper;

import com.hcs.domain.Club;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Param : mybatis 에서 sql 문장에 다수의 파라미터를 전달할 때 변수이름을 각각 지정해주기위해 사용한다.
 */

@Mapper
public interface ClubMapper {

    Club findById(long id);

    //TODO: return type 을 List 로 변경하기
    Club findByTitle(String title);

    int insertClub(Club club);

    Club findClubWithMembers(long id);

    Club findClubWithManagers(long id);

    Club findClubWithManagersAndMembers(long id);

    int joinMemberById(@Param("clubId") long clubId, @Param("memberId") long userId);

    int joinManagerById(@Param("clubId") long clubId, @Param("managerId") long userId);

    int deleteClub(@Param("clubId") long clubId, @Param("managerId") long userId);

    List<Club> findAllClubs();

    List<Club> findByPageAndCategory(long categoryId);

    long countByAllClubs();

    void updateClub(Club club);

    boolean checkClubManager(@Param("clubId") long clubId, @Param("managerId") long userId);

    boolean checkClubMember(@Param("clubId") long clubId, @Param("memberId") long userId);

    int updateMemberCount(@Param("id") long id, @Param("memberCount") int memberCount);

    int deleteMember(@Param("clubId")long clubId, @Param("memberId") long userId);

}
