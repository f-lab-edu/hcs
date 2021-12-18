package com.hcs.mapper;

import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@EnableEncryptableProperties
@Transactional
class ClubMapperTest {
    @Autowired
    ClubMapper clubMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SqlSession session;

    @DisplayName("ClubMapper - club db에 저장 및 찾기 테스트")
    @Test
    void saveAndFindByTitleTest() {
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.save(club);

        Club aClub = clubMapper.findByTitle("testClub");
        Club bClub = clubMapper.findByTitle("BClub");
        assertNotNull(aClub);
        assertNull(bClub);
    }

    @DisplayName("ClubMapper - club db에 저장 및 삭제 ")
    @Test
    void deleteTest() {
        Club club = Club.builder()
                .title("testDeleteClub")
                .location("Bucheon")
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.save(club);
        Club aClub = clubMapper.findByTitle("testDeleteClub");
        clubMapper.delete(aClub.getId());

        Club newClub = clubMapper.findByTitle("testDeleteClub");
        assertNull(newClub);
    }

    @DisplayName("ClubMapper - club member 저장 및 가져오기")
    @Test
    void findMembersTest() {
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.save(club);
        Club testClub = clubMapper.findByTitle("testClub");

        int memberSize = 3;
        Set<User> userSet = generateAndJoinClub(testClub, UserType.MEMBER, memberSize);

        Club aClubWithMembers = clubMapper.findClubWithMembers(testClub.getId());
        assertNotNull(aClubWithMembers);
        assertEquals(memberSize, aClubWithMembers.getMembers().size());

    }

    private void generateAndJoinClub(Club club, String target, int userSize) {
        for (int i = 0; i < userSize; i++) {
            String username = "testuser" + i;
            User user = User.builder()
                    .email(username + "@gmail.com")
                    .nickname(username)
                    .password(username + "pass").build();

            userMapper.insertUser(user);
            User newUser = userMapper.findByEmail(username + "@gmail.com");
            if (target.equals("member")) {
                clubMapper.joinMemberById(club.getId(), newUser.getId());
            } else if (target.equals("manager")) {
                clubMapper.joinManagerById(club.getId(), newUser.getId());
            }
        }
    }

    @DisplayName("ClubMapper - club manager 저장 및 가져오기")
    @Test
    void findManagersTest() {
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.save(club);
        Club testClub = clubMapper.findByTitle("testClub");

        int managerSize = 5;
        Set<User> userSet = generateAndJoinClub(testClub, UserType.MANAGER, managerSize);

        Club clubWithManagers = clubMapper.findClubWithManagers(testClub.getId());
        assertNotNull(clubWithManagers);
        assertEquals(managerSize, clubWithManagers.getManagers().size());

    }

    private Set<User> generateAndJoinClub(Club club, UserType userType, int userSize) {
        Set<User> userSet = new HashSet<>();
        for (int i = 0; i < userSize; i++) {
            String username = "testuser" + i;
            User user = User.builder()
                    .email(username + "@gmail.com")
                    .nickname(username)
                    .password(username + "pass").build();

            userMapper.insertUser(user);
            User newUser = userMapper.findByEmail(username + "@gmail.com");
            if (userType == UserType.MANAGER) {
                clubMapper.joinManagerById(club.getId(), newUser.getId());
            } else if (userType == UserType.MEMBER) {
                clubMapper.joinMemberById(club.getId(), newUser.getId());
            }
            userSet.add(user);
        }
        return userSet;
    }

    private List<Club> generateClub(int clubSize) {
        List<Club> clubList = new ArrayList<>();
        for (int i = 0; i < clubSize; i++) {
            Club club = Club.builder().title("testClub_" + i)
                    .createdAt(LocalDateTime.now())
                    .description("this is club for test")
                    .location("Mars")
                    .category("test")
                    .build();
            clubMapper.insertClub(club);
            clubList.add(club);
        }
        return clubList;
    }

    @DisplayName("mybatis mapper pagination test")
    @Test
    void findAllClubWithPageTest() {
        List<Club> clubList = generateClub(30);
        int limit = 5, start = 0;
        RowBounds rowBounds = new RowBounds(start, limit);
        List<Club> pagingClubList = session.selectList("com.hcs.mapper.ClubMapper.findClubWithPaging", null, rowBounds);
        assertEquals(pagingClubList.size(), limit);
        assertEquals(clubList.get(start).getId(), pagingClubList.get(0).getId());
        assertEquals(clubList.get(start + limit - 1).getId(), pagingClubList.get(limit - 1).getId()); // 페이징 마지막 값

        limit = 10;
        start = 20;
        rowBounds = new RowBounds(start, limit);
        pagingClubList = session.selectList("com.hcs.mapper.ClubMapper.findClubWithPaging", null, rowBounds);
        assertEquals(pagingClubList.size(), limit);
        assertEquals(clubList.get(start).getId(), pagingClubList.get(0).getId());
        assertEquals(clubList.get(start + limit - 1).getId(), pagingClubList.get(limit - 1).getId());
    }

    enum UserType {
        MANAGER,
        MEMBER
    }
}
