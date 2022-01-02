package com.hcs.mapper;

import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[MyBatisConfig]")})
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
                .categoryId(1L)
                .createdAt(LocalDateTime.now().withNano(0)) // 밀리초 단위 절삭
                .build();
        club.setCreatedAt(LocalDateTime.now());

        clubMapper.insertClub(club);

        Club aClub = clubMapper.findByTitle("testClub");
        Club bClub = clubMapper.findByTitle("BClub");

        assertEquals(club.getId(), aClub.getId());
        assertEquals(club.getTitle(), aClub.getTitle());
        assertEquals(club.getLocation(), aClub.getLocation());
        assertEquals(club.getDescription(), aClub.getDescription());
        //assertEquals(club.getCreatedAt(), aClub.getCreatedAt()); // TODO : 필드에 값 할당시 나노 초 단위 절삭 구현 또는 해당기능을 하는 annotation 추가하기

        assertEquals(club.getCategoryId(), aClub.getCategoryId());

        assertNull(bClub);
    }

    @DisplayName("ClubMapper - club db에 저장 및 삭제 ")
    @Test
    void deleteTest() {
        Club club = Club.builder()
                .title("testDeleteClub")
                .location("Bucheon")
                .categoryId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.insertClub(club);
        Club aClub = clubMapper.findByTitle("testDeleteClub");
        assertEquals(club.getId(), aClub.getId());
        clubMapper.deleteClubById(aClub.getId());

        Club newClub = clubMapper.findById(aClub.getId());
        assertNull(newClub);
    }

    @DisplayName("ClubMapper - club member 저장 및 가져오기")
    @Test
    void findMembersTest() {
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .categoryId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.insertClub(club);
        Club testClub = clubMapper.findByTitle("testClub");

        int memberSize = 3;

        Set<User> userSet = generateAndJoinClub(testClub, UserType.MEMBER, memberSize);
        Club aClubWithMembers = clubMapper.findClubWithMembers(testClub.getId());
        Set<User> memberSet = aClubWithMembers.getMembers();
        assertEquals(userSet, memberSet);
    }

    @DisplayName("ClubMapper - club manager 저장 및 가져오기")
    @Test
    void findManagersTest() {
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .categoryId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.insertClub(club);
        Club testClub = clubMapper.findByTitle("testClub");

        int managerSize = 5;

        Set<User> userSet = generateAndJoinClub(testClub, UserType.MANAGER, managerSize);
        Club clubWithManagers = clubMapper.findClubWithManagers(testClub.getId());
        assertEquals(userSet, clubWithManagers.getManagers());
    }

    @DisplayName("mybatis mapper pagination test")
    @Test
    void findAllClubWithPageTest() {
        List<Club> clubList = generateClubWithCategory(30, 1);
        int limit = 5, start = 0;
        RowBounds rowBounds = new RowBounds(start, limit);
        List<Club> pagingClubList = session.selectList("com.hcs.mapper.ClubMapper.findAllClubs", null, rowBounds);
        assertEquals(pagingClubList.size(), limit);
        assertEquals(clubList.get(start).getId(), pagingClubList.get(0).getId());
        assertEquals(clubList.get(start + limit - 1).getId(), pagingClubList.get(limit - 1).getId()); // 페이징 마지막 값

        limit = 10;
        start = 20;
        rowBounds = new RowBounds(start, limit);
        pagingClubList = session.selectList("com.hcs.mapper.ClubMapper.findAllClubs", null, rowBounds);
        assertEquals(pagingClubList.size(), limit);
        assertEquals(clubList.get(start).getId(), pagingClubList.get(0).getId());
        assertEquals(clubList.get(start + limit - 1).getId(), pagingClubList.get(limit - 1).getId());
    }

    @DisplayName("페이지를 적용해 특정카테고리의 club list 가져오기")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    void findByPageAndCategory(int count) {
        //given
        int start = 1;
        int givenClubSize = 10;
        RowBounds rowBounds = new RowBounds(start - 1, count);
        long givenCategoryId = 2;
        generateClubWithCategory(givenClubSize, givenCategoryId);

        //when
        List<Club> clubList = session.selectList("com.hcs.mapper.ClubMapper.findByPageAndCategory", givenCategoryId, rowBounds);

        //then
        assertEquals(clubList.size(), givenClubSize > count ? count : givenClubSize);
        for (Club c : clubList) {
            assertEquals(c.getCategoryId(), givenCategoryId);
        }
    }

    @DisplayName("모든 클럽 수 세기")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    void countByAllClubs(int givenClubSize){
        //given
        List<Club> givenClubList  = generateClubWithCategory(givenClubSize, 1);

        //when
        long totalClubCount = clubMapper.countByAllClubs();

        //then
        assertEquals(givenClubSize,totalClubCount);
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

    private List<Club> generateClubWithCategory(int clubSize, long categoryId) {
        List<Club> clubList = new ArrayList<>();
        for (int i = 0; i < clubSize; i++) {
            Club club = Club.builder().title("testClub_" + i)
                    .createdAt(LocalDateTime.now())
                    .description("this is club for test")
                    .location("Mars")
                    .categoryId(categoryId)
                    .build();
            clubMapper.insertClub(club);
            clubList.add(club);
        }
        return clubList;
    }

    enum UserType {
        MANAGER,
        MEMBER
    }
}
