package com.hcs.service;

import com.hcs.domain.Club;
import com.hcs.dto.request.ClubDto;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.dto.response.club.ClubInfoDto;
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
import org.springframework.test.util.ReflectionTestUtils;

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

    @Mock
    CategoryService categoryService;

    static Club fixtureClub;

    @BeforeAll
    static void init() { //TODO : 추후 test 용 fixer 만들기
        fixtureClub = Club.builder()
                .id(1L)
                .title("test club")
                .categoryId(1L)
                .createdAt(LocalDateTime.now())
                .description("description")
                .location("test location")
                .build();
    }

    @DisplayName("club dto를 db에 저장하기")
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
        given(categoryService.getCategoryId(eq("sports"))).willReturn(1L);

        //when
        Club savedClub = clubService.saveNewClub(correctClubDto);

        //then
        assertEquals(savedClub, fixtureClub);

    }

    @DisplayName("club id로 club 데이터를 가져오기")
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
        List<ClubInListDto> givenClubInListDtos = new ArrayList<>();
        List<Club> givnClubList = new ArrayList<>();
        ClubInListDto dto;
        for (int i = 0; i < count; i++) {
            givnClubList.add(new Club());
            dto = new ClubInListDto();
            dto.setClubId(1L);
            givenClubInListDtos.add(dto);
        }
        doReturn(givnClubList).when(sqlSession).selectList(eq("com.hcs.mapper.ClubMapper.findByPageAndCategory"), anyLong(), any(RowBounds.class));
        ClubInListDto clubInListDto = new ClubInListDto();
        clubInListDto.setClubId(1L);
        Club c = new Club();
        given(modelMapper.map(c, ClubInListDto.class)).willReturn(clubInListDto);
        String domainUrl = "https://localhost:8443/";
        ReflectionTestUtils.setField(clubService, "domainUrl", domainUrl); //private field 에 값 주입

        //when
        List<ClubInListDto> clubInListDtos = clubService.getClubListWithPagingAndCategory(page, count, categoryId);

        //then
        assertEquals(clubInListDtos.size(), count);
        assertEquals(clubInListDtos.get(0).getClubUrl(), domainUrl + "club/" + clubInListDtos.get(0).getClubId());
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

    @DisplayName("id 값이 주어지면 클럽 정보 반환하기")
    @Test
    void getClubInfo() {
        //given
        long givenClubId = fixtureClub.getId();
        ClubInfoDto givenDto = new ClubInfoDto();
        given(clubMapper.findById(fixtureClub.getId())).willReturn(fixtureClub);
        given(modelMapper.map(fixtureClub, ClubInfoDto.class)).willReturn(givenDto);
        given(categoryService.getCategoryName(anyLong())).willReturn(eq("sports"));
        String domainUrl = "https://localhost:8443/";
        ReflectionTestUtils.setField(clubService, "domainUrl", domainUrl); //private field 에 값 주입

        //when
        ClubInfoDto clubInfoDto = clubService.getClubInfo(givenClubId);

        //then
        assertEquals(clubInfoDto, givenDto);
        assertEquals(clubInfoDto.getClubUrl(), domainUrl + "club/" + fixtureClub.getId());

    }
}
