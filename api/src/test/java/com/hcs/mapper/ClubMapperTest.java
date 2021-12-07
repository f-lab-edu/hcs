package com.hcs.mapper;

import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        generateAndJoinClub(testClub, "member", memberSize);

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

            userMapper.save(user);
            User newUser = userMapper.findByEmail(username + "@gmail.com");
            if (target.equals("member"))
                clubMapper.joinMemberById(club.getId(), newUser.getId());
            else if (target.equals("manager"))
                clubMapper.joinManagerById(club.getId(), newUser.getId());
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
        generateAndJoinClub(testClub, "manager", managerSize);

        Club clubWithManagers = clubMapper.findClubWithManagers(testClub.getId());
        assertNotNull(clubWithManagers);
        assertEquals(managerSize, clubWithManagers.getManagers().size());

    }

}
