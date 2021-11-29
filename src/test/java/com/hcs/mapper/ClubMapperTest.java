package com.hcs.mapper;

import com.hcs.domain.Club;
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

    @DisplayName("ClubMapper - club db에 저장 및 타이틀로 찾기 ")
    @Test
    void saveAndFindByTitleTest() {
        Club club = Club.builder()
                .title("AClub")
                .location("Bucheon")
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.save(club);

        Club aClub = clubMapper.findByTitle("AClub");
        Club bClub = clubMapper.findByTitle("BClub");
        assertNotNull(aClub);
        assertNull(bClub);
    }

    @DisplayName("ClubMapper - club db에 저장 및 id로 찾기 테스트")
    @Test
    void saveAndFindByIdTest() {
        Club club = Club.builder()
                .title("AClub")
                .location("Bucheon")
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.save(club);

        Club club1 = clubMapper.findByTitle("AClub");
        Club club2 = clubMapper.findById(club.getId());

        assertEquals(club1,club2);

    }

    @DisplayName("ClubMapper - club db에 저장 및 삭제 ")
    @Test
    void deleteTest() {
        Club club = Club.builder()
                .title("AClub")
                .location("Bucheon")
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();

        clubMapper.save(club);
        Club aClub = clubMapper.findByTitle("AClub");
        clubMapper.delete(aClub.getId());

        Club newClub = clubMapper.findByTitle("AClub");
        assertNull(newClub);
    }
}
