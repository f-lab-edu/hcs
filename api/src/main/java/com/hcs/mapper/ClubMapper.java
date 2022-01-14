package com.hcs.mapper;

import com.hcs.domain.Club;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @Param : mybatis 에서 sql 문장에 다수의 파라미터를 전달할 때 변수이름을 각각 지정해주기위해 사용한다.
 */

@Mapper
public interface ClubMapper {

    Club findById(long id);

    //TODO: return type 을 List 로 변경하기
    Club findByTitle(String title);

    void insertClub(Club club);

    Club findClubWithMembers(long id);

    Club findClubWithManagers(long id);

    void joinMemberById(@Param("clubId") long clubId, @Param("memberId") long userId);

    void joinManagerById(@Param("clubId") long clubId, @Param("managerId") long userId);

    void deleteClub(long id);

    List<Club> findAllClubs();

    List<Club> findByPageAndCategory(long categoryId);

    long countByAllClubs();

    void updateClub(Club club);

    //TODO: sort

}
