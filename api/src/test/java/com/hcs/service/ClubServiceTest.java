package com.hcs.service;

import com.hcs.config.DomainUrlConfig;
import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.hcs.dto.request.ClubSubmitDto;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.dto.response.club.ClubInfoDto;
import com.hcs.dto.response.club.ClubJoinDto;
import com.hcs.mapper.ClubMapper;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

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

    @Mock
    UserService userService;

    @Mock
    DomainUrlConfig domainUrlConfig;

    Club fixtureClub;

    User fixtureUser;
    User fixtureUser_2;

    @BeforeEach
    void init() {
        fixtureClub = Club.builder()
                .id(1L)
                .title("test club")
                .categoryId(1L)
                .createdAt(LocalDateTime.now())
                .description("description")
                .location("test location")
                .build();
        fixtureUser = User.builder()
                .id(10L)
                .email("testuser@test.com")
                .nickname("testuser")
                .password("testPass")
                .build();
        fixtureUser_2 = User.builder()
                .id(11L)
                .email("testuser2@test.com")
                .nickname("testuser2")
                .password("testPass")
                .build();
    }

    @DisplayName("club dto를 db에 저장하기")
    @Test
    void saveNewClub() {
        //given
        ClubSubmitDto correctClubDto = new ClubSubmitDto(
                "club title",
                "club description",
                "test location",
                "sports");
        User manager = User.builder().id(1L)
                .email("testUser@gmail.com")
                .location("testloca")
                .age(1)
                .nickname("testuser")
                .password("1234qwer").build();

        given(modelMapper.map(correctClubDto, Club.class)).willReturn(fixtureClub);
        given(categoryService.getCategoryId(eq("sports"))).willReturn(1L);
        given(clubMapper.insertClub(any(Club.class))).willReturn(1);
        given(clubMapper.joinManagerById(anyLong(), anyLong())).willReturn(1);

        //when
        Club savedClub = clubService.saveNewClub(correctClubDto, manager.getId());

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
            givnClubList.add(fixtureClub);
            dto = new ClubInListDto();
            dto.setClubId(1L);
            givenClubInListDtos.add(dto);
        }
        doReturn(givnClubList).when(sqlSession).selectList(eq("com.hcs.mapper.ClubMapper.findByPageAndCategory"), anyLong(), any(RowBounds.class));
        ClubInListDto clubInListDto = new ClubInListDto();
        clubInListDto.setClubId(1L);
        given(modelMapper.map(fixtureClub, ClubInListDto.class)).willReturn(clubInListDto);
        String domainUrl = "https://localhost:8443/";
        given(domainUrlConfig.getUrl()).willReturn(domainUrl);
        given(categoryService.getCategoryName(anyLong())).willReturn("sports");

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
        String domainUrl = "https://localhost:8443/";
        given(domainUrlConfig.getUrl()).willReturn(domainUrl);
        given(categoryService.getCategoryName(anyLong())).willReturn(eq("sports"));

        //when
        ClubInfoDto clubInfoDto = clubService.getClubInfo(givenClubId);

        //then
        assertEquals(clubInfoDto, givenDto);
        assertEquals(clubInfoDto.getClubUrl(), domainUrl + "club/" + fixtureClub.getId());

    }

    @DisplayName("저장되있는 club id 와 바꿀 정보 가 담긴 Dto 객체가 주어지면, update 한 club id 반환하기")
    @Test
    void modifyClub() {
        //given
        ClubSubmitDto clubDto = new ClubSubmitDto(
                "modify title",
                "description",
                "location",
                "sports"
        );

        Club modifiedClub = Club.builder()
                .id(fixtureClub.getId())
                .title(clubDto.getTitle())
                .description(clubDto.getDescription())
                .categoryId(1L)
                .location(clubDto.getLocation())
                .build();

        given(categoryService.getCategoryId("sports")).willReturn(1L);
        given(modelMapper.map(clubDto, Club.class)).willReturn(modifiedClub);
        given(clubMapper.findById(anyLong())).willReturn(fixtureClub);

        //when
        long modifyClubId = clubService.modifyClub(fixtureClub.getId(), clubDto);

        //then
        assertEquals(modifyClubId, fixtureClub.getId());
    }

    @DisplayName("삭제할 clubId 와 managerId 가 주어지면, 삭제한 clubId 반환하기")
    @Test
    void deleteClub() {
        //given
        long givenClubId = fixtureClub.getId();
        long givenManagerId = 2L;
        given(clubMapper.findById(givenClubId)).willReturn(fixtureClub);
        given(clubMapper.deleteClub(givenClubId)).willReturn(1);
        given(clubMapper.checkClubManager(givenClubId, givenManagerId)).willReturn(true);

        //when
        long deletedClubId = clubService.deleteClub(givenClubId, givenManagerId);
        //then
        assertEquals(deletedClubId, givenClubId);
    }

    @DisplayName("club 이 주어지면, memberCount 를 1 올리기")
    @Test
    void plusMemberCount() {
        //given
        Club club = fixtureClub;
        given(clubMapper.updateMemberCount(anyLong(), anyInt())).willReturn(1);
        //when
        clubService.plusMemberCount(club);
        //then
        then(clubMapper).should(times(1)).updateMemberCount(club.getId(), club.getMemberCount());
    }

    @DisplayName("club 과 user 가 주어지면, 해당 user 가 member 이거나  manager 인지 확인")
    @Test
    void checkAlreadyJoinedClub() {
        //given
        Club club = fixtureClub;
        User user = fixtureUser;
        //when
        clubService.checkAlreadyJoinedClub(club, user);
        //then
        then(clubMapper).should(times(1)).checkClubManager(club.getId(), user.getId());
        then(clubMapper).should(times(1)).checkClubMember(club.getId(), user.getId());

    }

    @DisplayName("clubId 와 user 가 주어지면 user 를 member 로 등록")
    @Test
    void joinClub() {
        //given
        Club club = fixtureClub;
        int plusMemberCount = 1;
        User user = fixtureUser;
        given(clubMapper.findById(club.getId())).willReturn(club);
        given(clubMapper.checkClubManager(club.getId(), user.getId())).willReturn(false);
        given(clubMapper.checkClubMember(club.getId(), user.getId())).willReturn(false);
        given(clubMapper.updateMemberCount(club.getId(), plusMemberCount)).willReturn(1);
        given(clubMapper.joinMemberById(club.getId(), user.getId())).willReturn(1);
        //when
        ClubJoinDto dto = clubService.joinClub(club.getId(), user);
        //then
        assertEquals(dto.getApplicantId(), user.getId());
        assertEquals(dto.getCurrentMembersCount(), plusMemberCount);
    }

    @DisplayName("clubId 와 userId, managerEmail이 주어지면, user를 member에서 삭제")
    @Test
    void expulsionMember() {
        //given
        User manager = fixtureUser;
        User user = fixtureUser_2;
        Club givenClub = fixtureClub;
        int beforeMemberCount = 1;
        givenClub.setMemberCount(beforeMemberCount);
        given(clubMapper.findById(givenClub.getId())).willReturn(givenClub);
        given(userService.findById(anyLong())).willReturn(new User());
        given(userService.findByEmail(any())).willReturn(new User());
        given(clubMapper.checkClubManager(anyLong(), anyLong())).willReturn(true);
        given(clubMapper.deleteMember(anyLong(), anyLong())).willReturn(1);
        given(clubMapper.updateMemberCount(anyLong(), anyInt())).willReturn(1);

        //when
        Club club = clubService.expulsionMember(givenClub.getId(), manager.getEmail(), user.getId());
        //then
        then(clubMapper).should(times(2)).checkClubManager(anyLong(), anyLong());
        then(clubMapper).should(times(1)).deleteMember(anyLong(), anyLong());
        then(clubMapper).should(times(1)).updateMemberCount(anyLong(), anyInt());
        assertEquals(beforeMemberCount - 1, club.getMemberCount());

    }

    @DisplayName("clubId 와 member id 가 주어지면,  club member에서 삭제")
    @Test
    void resignMember() {
        //given
        User user = fixtureUser_2;
        Club givenClub = fixtureClub;
        int beforeMemberCount = 1;
        givenClub.setMemberCount(beforeMemberCount);
        given(clubMapper.findById(givenClub.getId())).willReturn(givenClub);
        given(clubMapper.checkClubMember(anyLong(), anyLong())).willReturn(true);
        given(clubMapper.deleteMember(anyLong(), anyLong())).willReturn(1);
        given(clubMapper.updateMemberCount(anyLong(), anyInt())).willReturn(1);

        //when
        Club club = clubService.resignMember(givenClub.getId(), user.getId());

        //then
        then(clubMapper).should(times(1)).checkClubMember(anyLong(), anyLong());
        then(clubMapper).should(times(1)).deleteMember(anyLong(), anyLong());
        then(clubMapper).should(times(1)).updateMemberCount(anyLong(), anyInt());
        assertEquals(beforeMemberCount - 1, club.getMemberCount());

    }

    @DisplayName("club id 와 managerEmail, member id 가 주어지면 member 를 manager 로 만들기")
    @ParameterizedTest
    @ValueSource(ints = {5, 10, 20})
    void makeManager(int givenNumber) {
        //given
        Club givenClub = fixtureClub;
        givenClub.setManagerCount(givenNumber);
        givenClub.setMemberCount(givenNumber);
        User givenManager = fixtureUser;
        User givenMember = fixtureUser_2;
        given(clubMapper.findById(givenClub.getId())).willReturn(givenClub);
        given(userService.findByEmail(any())).willReturn(givenManager);
        given(userService.findById(anyLong())).willReturn(givenMember);
        given(clubMapper.checkClubManager(anyLong(), eq(givenManager.getId()))).willReturn(true);
        given(clubMapper.checkClubMember(anyLong(), eq(givenMember.getId()))).willReturn(true);
        given(clubMapper.deleteMember(anyLong(), anyLong())).willReturn(1);
        given(clubMapper.updateMemberCount(anyLong(), anyInt())).willReturn(1);
        given(clubMapper.joinManagerById(anyLong(), eq(givenMember.getId()))).willReturn(1);
        given(clubMapper.updateManagerCount(anyLong(), anyInt())).willReturn(1);
        //when
        Club club = clubService.makeManager(givenClub.getId(), givenManager.getEmail(), givenMember.getId());
        //then
        then(clubMapper).should(times(1)).joinManagerById(anyLong(), anyLong());
        then(clubMapper).should(times(1)).deleteMember(anyLong(), anyLong());
        then(clubMapper).should(times(1)).updateMemberCount(anyLong(), anyInt());
        then(clubMapper).should(times(1)).updateManagerCount(anyLong(), anyInt());
        assertEquals(club.getManagerCount(), givenNumber + 1);
        assertEquals(club.getMemberCount(), givenNumber - 1);
    }

}
