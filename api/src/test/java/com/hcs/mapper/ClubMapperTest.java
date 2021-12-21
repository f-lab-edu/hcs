package com.hcs.mapper;

import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest(includeFilters = {@ComponentScan.Filter(classes = {Configuration.class, org.apache.ibatis.annotations.Mapper.class, Bean.class})})
class ClubMapperTest {

    @Autowired
    ClubMapper clubMapper;
    @Autowired
    UserMapper userMapper;

    @DisplayName("ClubMapper - club db에 저장 및 찾기 테스트")
    @Test
    void saveAndFindByTitleTest() {
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .category("test category")
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
        assertEquals(club.getCreatedAt(), aClub.getCreatedAt());
        assertEquals(club.getCategory(), aClub.getCategory());

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
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.insertClub(club);
        Club testClub = clubMapper.findByTitle("testClub");

        int memberSize = 3;
        Set<User> userSet = generateAndJoinClub(testClub, "member", memberSize);

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
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.insertClub(club);
        Club testClub = clubMapper.findByTitle("testClub");

        int managerSize = 5;
        Set<User> userSet = generateAndJoinClub(testClub, "manager", managerSize);

        Club clubWithManagers = clubMapper.findClubWithManagers(testClub.getId());
        assertEquals(userSet, clubWithManagers.getManagers());
    }


    private Set<User> generateAndJoinClub(Club club, String userType, int userSize) {
        Set<User> userSet = new HashSet<>();
        for (int i = 0; i < userSize; i++) {
            String username = "testuser" + i;
            User user = User.builder()
                    .email(username + "@gmail.com")
                    .nickname(username)
                    .password(username + "pass").build();

            userMapper.insertUser(user);
            User newUser = userMapper.findByEmail(username + "@gmail.com");
            if (userType.equals("member")) {
                clubMapper.joinMemberById(club.getId(), newUser.getId());
            } else if (userType.equals("manager")) {
                clubMapper.joinManagerById(club.getId(), newUser.getId());
            }
            userSet.add(user);
        }
        return userSet;
    }

}
