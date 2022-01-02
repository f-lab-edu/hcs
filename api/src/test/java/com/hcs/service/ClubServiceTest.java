package com.hcs.service;

import com.hcs.domain.Club;
import com.hcs.dto.request.ClubDto;
import com.hcs.mapper.ClubMapper;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {

    @InjectMocks
    ClubService clubService;

    @Mock
    ClubMapper clubMapper;

    @Mock
    ModelMapper modelMapper;

    @Mock
    SqlSession sqlSession;

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
                "sports");

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

    @DisplayName("페이지숫자, 개수가 주어지면 clubList 반환하기")
    @Test
    void getClubListWithPagingAndCategory() {
        //given
        int page = 2;
        int count = 10;
        long categoryId = 2;
        List<Club> givenClubList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            givenClubList.add(new Club());
        }
        doReturn(givenClubList).when(sqlSession).selectList(eq("com.hcs.mapper.ClubMapper.findByPageAndCategory"), anyLong(), any(RowBounds.class));

        //when
        List<Club> clubList = clubService.getClubListWithPagingAndCategory(page, count, categoryId);

        //then
        assertEquals(givenClubList, clubList);
        assertEquals(clubList.size(), count);
    }

    @DisplayName("전체 club 개수 반환하기")
    @Test
    void getAllClubCounts() {
        //given
        long givenTotalClubCount = 10;
        given(clubMapper.countByAllClubs()).willReturn(givenTotalClubCount);

        //when
        long totalClubCount = clubService.getAllClubCounts();

        //then
        assertEquals(totalClubCount, givenTotalClubCount);
    }
}
