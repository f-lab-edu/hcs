package com.hcs.mapper;

import com.hcs.common.JdbcTemplateHelper;
import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = {".*DataSourceConfig", ".*JasyptConfig", ".*Helper"})})
class ClubMapperTest {

    @Autowired
    ClubMapper clubMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SqlSession session;
    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;

    User fixtureUser1;

    User fixtureUser2;

    Club fixtureClub;

    User fixtureManager;

    @BeforeEach
    void initFixture() {
        long user1Id = jdbcTemplateHelper.insertTestUser("fixtureUser1@test.com", "fixUser1", "testpass");
        fixtureUser1 = jdbcTemplateHelper.selectTestUser(user1Id);

        long user2Id = jdbcTemplateHelper.insertTestUser("fixtureUser2@test.com", "fixUser2", "testpass");
        fixtureUser2 = jdbcTemplateHelper.selectTestUser(user2Id);

        long clubId = jdbcTemplateHelper.insertTestClub("fixtureClub", "test loc", 1L);
        fixtureClub = jdbcTemplateHelper.selectTestClub(clubId);

        long managerId = jdbcTemplateHelper.insertTestUser("fixtureManager@test.com", "fixManager", "testpass");
        fixtureManager = jdbcTemplateHelper.selectTestUser(managerId);
        jdbcTemplateHelper.insertTestClubManagers(clubId, managerId);
        jdbcTemplateHelper.updateTestClub_managerCount(clubId, 1);

    }

    @DisplayName("ClubMapper - club db에 저장 및 찾기 테스트")
    @Test
    void saveAndFindByTitleTest() {
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .categoryId(1L)
                .createdAt(LocalDateTime.now().withNano(0)) // 밀리초 단위 절삭
                .managerCount(1)
                .build();
        club.setCreatedAt(LocalDateTime.now());

        int result = clubMapper.insertClub(club);

        Club aClub = clubMapper.findByTitle("testClub");
        Club bClub = clubMapper.findByTitle("BClub");

        assertEquals(club.getId(), aClub.getId());
        assertEquals(club.getTitle(), aClub.getTitle());
        assertEquals(club.getLocation(), aClub.getLocation());
        assertEquals(club.getDescription(), aClub.getDescription());
        assertEquals(club.getManagerCount(), aClub.getManagerCount());

        assertEquals(club.getCategoryId(), aClub.getCategoryId());
        assertNull(bClub);

        int successReturnNum = 1; //insert 구문 성공시 1이 반환됨
        assertEquals(result, successReturnNum);
    }

    @DisplayName("ClubMapper - 삭제 ")
    @Test
    void deleteTest() {
        //given
        Club club = fixtureClub;
        User manager = fixtureManager;

        //when
        int result = clubMapper.deleteClub(club.getId(), manager.getId());

        //then
        Club newClub = clubMapper.findById(club.getId());
        assertNull(newClub);
        assertEquals(result, 1);
    }

    @DisplayName("ClubMapper - club member 저장 및 가져오기")
    @Test
    void findMembersTest() {
        //given
        Club testClub = fixtureClub;
        int memberSize = 3;
        Set<User> userSet = new HashSet<>();
        for (int i = 0; i < memberSize; i++) {
            long userId = jdbcTemplateHelper.insertTestUser("testUSer" + i + "@test.com", "testUSer" + i, "testpass");
            userSet.add(jdbcTemplateHelper.selectTestUser(userId));
            jdbcTemplateHelper.insertTestClubMembers(testClub.getId(), userId);
            int currentMemberCount = jdbcTemplateHelper.selectTestClub(testClub.getId()).getMemberCount();
            jdbcTemplateHelper.updateTestClub_memberCount(testClub.getId(), currentMemberCount + 1);
        }

        //when
        Club aClubWithMembers = clubMapper.findClubWithMembers(testClub.getId());

        //then
        Set<User> memberSet = aClubWithMembers.getMembers();
        assertEquals(userSet, memberSet);
    }

    @DisplayName("ClubMapper - club manager 저장 및 가져오기")
    @Test
    void findManagersTest() {
        //given
        long clubId = jdbcTemplateHelper.insertTestClub("test_Club", "test loc", 1);
        Club testClub = jdbcTemplateHelper.selectTestClub(clubId);
        int managerSize = 5;
        Set<User> userSet = new HashSet<>();
        for (int i = 0; i < managerSize; i++) {
            long userId = jdbcTemplateHelper.insertTestUser("testUset" + i + "@test.com", "testnick" + i, "testpass");
            userSet.add(jdbcTemplateHelper.selectTestUser(userId));
            jdbcTemplateHelper.insertTestClubManagers(testClub.getId(), userId);
        }

        //when
        Club clubWithManagers = clubMapper.findClubWithManagers(testClub.getId());

        //then
        assertEquals(userSet, clubWithManagers.getManagers());

    }

    @DisplayName("mybatis mapper pagination test")
    @Test
    void findAllClubWithPageTest() {
        jdbcTemplateHelper.deleteTestClub(fixtureClub.getId());
        int clubSize = 30;
        List<Club> clubList = new ArrayList<>();
        for (int i = 0; i < clubSize; i++) {
            long clubId = jdbcTemplateHelper.insertTestClub("testClub_" + i, "test loc", 1);
            clubList.add(jdbcTemplateHelper.selectTestClub(clubId));
        }

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

        for (int i = 0; i < givenClubSize; i++) {
            long clubId = jdbcTemplateHelper.insertTestClub("testClub_" + i, "test loc", givenCategoryId);
        }

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
    void countByAllClubs(int givenClubSize) {
        //given
        long beforeClubCount = jdbcTemplateHelper.selectCountAllTestClub();
        for (int i = 0; i < givenClubSize; i++) {
            long clubId = jdbcTemplateHelper.insertTestClub("testClub_" + i, "test loc", 1);
        }

        //when
        long totalClubCount = clubMapper.countByAllClubs();

        //then
        assertEquals(givenClubSize + beforeClubCount, totalClubCount);
    }

    @DisplayName("club update")
    @Test
    void updateClub() {
        //given
        long initCategoryId = 1L;
        long clubId = jdbcTemplateHelper.insertTestClub("testClub", "test loc", initCategoryId);
        Club givenClub = jdbcTemplateHelper.selectTestClub(clubId);
        long changedCategoryId = 2L;
        String changedDescription = "changed description at " + LocalDateTime.now().getSecond() + "sec";
        String changedLocation = "changed at " + LocalDateTime.now().getSecond() + "sec";
        String changedTitle = "changed title at " + LocalDateTime.now().getSecond() + "sec";
        givenClub.setCategoryId(changedCategoryId);
        givenClub.setDescription(changedDescription);
        givenClub.setLocation(changedLocation);
        givenClub.setTitle(changedTitle);

        //when
        clubMapper.updateClub(givenClub);

        //then
        Club modifiedClub = jdbcTemplateHelper.selectTestClub(givenClub.getId());
        assertEquals(givenClub.getId(), modifiedClub.getId());
        assertEquals(changedTitle, modifiedClub.getTitle());
        assertEquals(changedDescription, modifiedClub.getDescription());
        assertEquals(changedCategoryId, modifiedClub.getCategoryId());
        assertEquals(changedLocation, modifiedClub.getLocation());
        assertNotEquals(changedCategoryId, initCategoryId);
    }

    @DisplayName(" club id 와 user id 가 주어지면 manager 등록하기")
    @Test
    void joinManagerById() {
        long clubId = jdbcTemplateHelper.insertTestClub("testClub", "test loc", 1);
        Club club = jdbcTemplateHelper.selectTestClub(clubId);
        User user = fixtureUser1;

        int result = clubMapper.joinManagerById(club.getId(), user.getId());

        assertEquals(result, 1);

        int managerCount = jdbcTemplateHelper.selectTestManagerCountAtClubManagers(clubId, user.getId());

        assertEquals(managerCount, 1);
    }

    @DisplayName("club id 와 user id 가 주어지면 해당 user 가 manager 인지 확인하기 ")
    @Test
    void checkClubManager() {
        Club club = fixtureClub;
        User manager = fixtureManager;

        //club 에 등록된 user 체크
        boolean result = clubMapper.checkClubManager(club.getId(), manager.getId());

        assertTrue(result);

        //club 에 등록되지 않은 user 체크
        User testUser = fixtureUser1;
        boolean wrongResult = clubMapper.checkClubManager(club.getId(), testUser.getId());
        assertFalse(wrongResult);

    }

    @DisplayName("club id 와 user id 가 주어지면 해당 user 가 mamber 인지 확인하기 ")
    @Test
    void checkClubMember() {
        Club club = fixtureClub;
        User member = fixtureUser1;
        jdbcTemplateHelper.insertTestClubMembers(club.getId(), member.getId());

        //club 에 등록된 user 체크
        boolean result = clubMapper.checkClubMember(club.getId(), member.getId());

        assertTrue(result);

        //club 에 등록되지 않은 user 체크
        User testUser = fixtureUser2;
        boolean wrongResult = clubMapper.checkClubMember(club.getId(), testUser.getId());
        assertFalse(wrongResult);

    }

    @DisplayName("club id 와 user id 가 주어지면 member 등록하기")
    @Test
    void joinMember() {
        Club club = fixtureClub;
        User user = fixtureUser1;

        int result = clubMapper.joinMemberById(club.getId(), user.getId());

        assertEquals(result, 1);

        int memberCount = jdbcTemplateHelper.selectTestMemberCountAtClubMembers(club.getId(), user.getId());

        assertEquals(memberCount, 1);

    }

    @DisplayName(" club id 와 숫자가 주어지면, memberCount update 하기")
    @Test
    void updateMemberCount() {
        Club club = fixtureClub;
        int memberCount = club.getMemberCount();
        int updatedMemberCount = memberCount + 1;

        int result = clubMapper.updateMemberCount(club.getId(), updatedMemberCount);

        assertEquals(result, 1);

        int currentMemberCount = jdbcTemplateHelper.selectTestClub(club.getId()).getMemberCount();
        assertEquals(updatedMemberCount, currentMemberCount);
    }

    @DisplayName(" club id 와 member id 가 주어지면 member delete 하기")
    @Test
    void deleteMember() {
        //given
        Club club = fixtureClub;
        User member = fixtureUser1;
        jdbcTemplateHelper.insertTestClubMembers(club.getId(), member.getId());
        ;

        //when
        int result = clubMapper.deleteMember(club.getId(), member.getId());

        //them
        assertEquals(result, 1);
        int clubMemberCount = jdbcTemplateHelper.selectTestMemberCountAtClubMembers(club.getId(), member.getId());
        assertEquals(clubMemberCount, 0);
    }

}
