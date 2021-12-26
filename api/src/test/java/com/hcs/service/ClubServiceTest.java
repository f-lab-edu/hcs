package com.hcs.service;

import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.mapper.ClubMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {

    @InjectMocks
    ClubService clubService;

    @Mock
    ClubMapper clubMapper;
    @Mock
    ModelMapper modelMapper;

    static Club fixtureClub;

    @BeforeAll
    static void init() { //TODO : 추후 test 용 fixer 만들기기
        fixtureClub = Club.builder().id(1L).title("test club").build();
    }

    @DisplayName("club dto를 db에 저장한다.")
    @Test
    void saveNewClub() {
        //given
        ClubDto correctClubDto = new ClubDto(
                "club title",
                "club description",
                LocalDateTime.now(),
                "test location",
                "test category");

        given(modelMapper.map(correctClubDto, Club.class)).willReturn(fixtureClub);

        //when
        Club savedClub = clubService.saveNewClub(correctClubDto);

        //then
        assertEquals(savedClub, fixtureClub);

    }

    @DisplayName("club id로 club 데이터를 가져온다.")
    @Test
    void getClub() {
        //given
        long testId = fixtureClub.getId(); // 테스트용 id
        given(clubMapper.findById(testId)).willReturn(fixtureClub);

        //when
        Club club = clubService.getClub(testId);

        //then
        assertEquals(club, fixtureClub);

    }
}
