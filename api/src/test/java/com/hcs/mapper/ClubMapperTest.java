package com.hcs.mapper;

import com.hcs.common.UserType;
import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[DataSourceConfig]")
        , @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[Hcs].*")})
class ClubMapperTest {

    @Autowired
    ClubMapper clubMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SqlSession session;
    @Autowired
    JdbcTemplate jdbcTemplate;

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
        List<Club> clubList = generateClubBySizeAndCategoryId(1, 1L);
        Club club = clubList.get(0);
        Set<User> userSet = generateUserAndJoinClub(club, UserType.MANAGER, 1);
        Iterator<User> iter = userSet.iterator();
        User manager = iter.next();

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
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .categoryId(1L)
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.insertClub(club);
        Club testClub = clubMapper.findByTitle("testClub");

        int memberSize = 3;

        Set<User> userSet = generateUserAndJoinClub(testClub, UserType.MEMBER, memberSize);
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

        Set<User> userSet = generateUserAndJoinClub(testClub, UserType.MANAGER, managerSize);
        Club clubWithManagers = clubMapper.findClubWithManagers(testClub.getId());
        assertEquals(userSet, clubWithManagers.getManagers());
    }

    @DisplayName("mybatis mapper pagination test")
    @Test
    void findAllClubWithPageTest() {
        List<Club> clubList = generateClubBySizeAndCategoryId(30, 1);
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
        generateClubBySizeAndCategoryId(givenClubSize, givenCategoryId);

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
        List<Club> givenClubList = generateClubBySizeAndCategoryId(givenClubSize, 1);

        //when
        long totalClubCount = clubMapper.countByAllClubs();

        //then
        assertEquals(givenClubSize, totalClubCount);
    }

    @DisplayName("club update")
    @Test
    void updateClub() {
        //given
        long initCategoryId = 1L;
        List<Club> clubList = generateClubBySizeAndCategoryId(1, initCategoryId);
        Club givenClub = clubList.get(0);
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
        List<Club> list = jdbcTemplate.query("select * from Club where id=" + givenClub.getId()
                , (rs, rowNum) -> Club.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .categoryId(rs.getLong("categoryId"))
                        .location(rs.getString("location"))
                        .build());
        Club modifiedClub = list.get(0);
        assertEquals(givenClub.getId(), modifiedClub.getId());
        assertEquals(changedTitle, modifiedClub.getTitle());
        assertEquals(changedDescription, modifiedClub.getDescription());
        assertEquals(changedCategoryId, modifiedClub.getCategoryId());
        assertEquals(changedLocation, modifiedClub.getLocation());
        assertNotEquals(changedCategoryId, initCategoryId);
    }

    private Set<User> generateUserAndJoinClub(Club club, UserType userType, int userSize) {
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

    private List<Club> generateClubBySizeAndCategoryId(int clubSize, long categoryId) {
        String insertSql = "insert into Club (title, createdAt, categoryId, location) \n" +
                "values(?,?,?,?)";
        for (int i = 0; i < clubSize; i++) {
            jdbcTemplate.update(insertSql, new Object[]{"testClub_" + i, LocalDateTime.now(), categoryId, "test location"});

        }
        String selectAllClubs = "select * from Club";
        List<Club> clubList = jdbcTemplate.query(selectAllClubs,
                (rs, rowNum) -> Club.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .categoryId(rs.getLong("categoryId"))
                        .location(rs.getString("location"))
                        .createdAt(LocalDateTime.now())
                        .build()); // id 값을 가져오기위해 재검색
        return clubList;
    }
}
