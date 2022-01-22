package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.common.JdbcTemplateHelper;
import com.hcs.common.UserType;
import com.hcs.config.DomainUrlConfig;
import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.hcs.dto.request.ClubSubmitDto;
import com.hcs.exception.ErrorCode;
import com.hcs.mapper.ClubMapper;
import com.hcs.mapper.UserMapper;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc
@EnableEncryptableProperties
@Transactional
class ClubControllerTest {

    private static ClubSubmitDto clubDto = new ClubSubmitDto();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClubMapper clubMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;
    @Autowired
    private DomainUrlConfig domainUrlConfig;

    User fixtureUser1;

    static Stream<Arguments> stringListProvider() {
        return Stream.of(
                arguments("1", "123456789012345678901234567890123456789012345678901", " ", "", Arrays.asList("title", "description", "location", "category")),
                arguments("12345678901234567890123456789012345678901", "", "123456789012345678901", "sports", Arrays.asList("title", "location"))
        );
    }

    @BeforeEach
    void initFixture() {
        fixtureUser1 = User.builder()
                .nickname("user1")
                .password("user1")
                .email("user1@test.com")
                .age(1)
                .location("1 loc")
                .build();
    }

    @DisplayName("Club Submit - 새로운 club 생성과 manager join")
    @Test
    void createClub() throws Exception {
        clubDto.setTitle("새마을농구동호회");
        clubDto.setDescription("농구하고싶은사람 모여라");
        clubDto.setCategory("sports");
        clubDto.setLocation("Bucheon");
        userMapper.insertUser(fixtureUser1);

        MvcResult mvcResult = mockMvc.perform(post("/club/submit")
                        .param("userEmail", fixtureUser1.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.HCS.status").value("200"))
                .andExpect(jsonPath("$.HCS.item.clubId").exists())
                .andReturn();

        long clubId = Long.parseLong(JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.HCS.item.clubId").toString());
        Club newClub = clubMapper.findClubWithManagers(clubId);
        Iterator<User> iterator = newClub.getManagers().iterator();
        User manager = iterator.next();
        assertEquals(fixtureUser1.getId(), manager.getId());
        assertEquals(newClub.getManagerCount(), newClub.getManagers().size());

    }

    @DisplayName("Club Submit - ClobSubmitDto valid 요류 응답")
    @ParameterizedTest(name = "#{index} - {displayName} = Test with Argument={0}, {1}, {2}, {3}")
    @MethodSource("stringListProvider")
    void createClub_valid_error_response(String title, String description, String location, String category, List<String> invalidFields) throws Exception {
        clubDto.setTitle(title);
        clubDto.setDescription(description);
        clubDto.setLocation(location);
        clubDto.setCategory(category);

        MvcResult mvcResult = mockMvc.perform(post("/club/submit")
                        .param("userEmail", fixtureUser1.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int length = JsonPath.parse(response).read("$.HCS.item.errors.length()");

        for (int i = 0; i < length; i++) {
            String field = JsonPath.parse(response).read("$.HCS.item.errors[" + i + "].field");
            assertThat(invalidFields).contains(field);
        }
    }

    @DisplayName("ClubInfo - 클럽 정보 요청")
    @Test
    void clubInfo() throws Exception {
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .categoryId(1L)
                .createdAt(LocalDateTime.now())
                .build();
        clubMapper.insertClub(club);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/club/info")
                        .param("clubId", club.getId().toString())//올바른 id
                        .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$.HCS.item.club.title").value(club.getTitle()))
                .andExpect(jsonPath("$.HCS.status").value("200"))
                .andExpect(jsonPath("$.HCS.item.clubId").exists())
                .andExpect(jsonPath("$.HCS.item.club.location").value(club.getLocation()))
                .andReturn();

        //clubUrl 검증
        String requestUrl = domainUrlConfig.getUrl() + "club/" + club.getId();
        String responseJsonClubUrl = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.HCS.item.club.clubUrl");
        assertEquals(requestUrl, responseJsonClubUrl);

        //TODO : managers , members 기능 추가 후 테스트 수정

    }

    @DisplayName("ClubList - 특정 카테고리의 club list를 페이징을 사용해 가져오기")
    @Test
    void clubList() throws Exception {
        //given
        int page = 1, generatedClubSize = 20;
        long givenCategoryId = 1;
        String givenCategory = "sports";
        List<Club> generatedClubList = jdbcTemplateHelper.generateTestClubBySizeAndCategoryId(generatedClubSize, givenCategoryId);

        //when
        mockMvc.perform(get("/club/list")
                        .param("page", String.valueOf(page))
                        .param("category", givenCategory))
                //then
                .andExpect(jsonPath("$.HCS.item.category").value(givenCategory))
                .andExpect(jsonPath("$.HCS.item.page").value(page))
                .andExpect(jsonPath("$.HCS.item.totalCount").value(generatedClubSize))
                .andDo(print());

        assertEquals(generatedClubList.get(0).getCategoryId(), givenCategoryId);

    }

    @DisplayName("Club modify - 클럽 정보 업데이트")
    @Test
    void modifyClub() throws Exception {
        List<Club> clubList = jdbcTemplateHelper.generateTestClubBySizeAndCategoryId(1, 1L);
        long clubId = clubList.get(0).getId();
        String changedCategory = "study";
        ClubSubmitDto clubSubmitDto = new ClubSubmitDto();
        String changedDescription = "changed description at " + LocalDateTime.now().getSecond() + "sec";
        String changedLocation = "changed at " + LocalDateTime.now().getSecond() + "sec";
        String changedTitle = "changed title at " + LocalDateTime.now().getSecond() + "sec";
        clubSubmitDto.setCategory(changedCategory);
        clubSubmitDto.setDescription(changedDescription);
        clubSubmitDto.setLocation(changedLocation);
        clubSubmitDto.setTitle(changedTitle);

        mockMvc.perform(put("/club/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("clubId", String.valueOf(clubId))
                        .content(objectMapper.writeValueAsString(clubSubmitDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.item.clubId").value(clubId));

    }

    @DisplayName("Club delete - manager 가 아닌 user 요청, manager 인 user 요청")
    @Test
    void deleteClub() throws Exception {
        List<Club> clubList = jdbcTemplateHelper.generateTestClubBySizeAndCategoryId(1, 1L);
        Club club = clubList.get(0);
        Set<User> userSet = jdbcTemplateHelper.generateTestUserAndJoinClub(club, UserType.MANAGER, 1);
        Iterator<User> iter = userSet.iterator();
        User manager = iter.next();

        //잘못된 접근 : manager 아닌 user 가 delete 요청
        User newUser = User.builder().email("newUserTest0@gmail.com")
                .nickname("newUser")
                .location("location")
                .password("qwer1234")
                .age(12)
                .joinedAt(LocalDateTime.now()).build();
        userMapper.insertUser(newUser);

        mockMvc.perform(delete("/club/delete")
                        .param("clubId", club.getId().toString())
                        .param("userEmail", newUser.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.item.errorCode").value(ErrorCode.CLUB_ACCESS_DENIED.getErrorCode()))
                .andExpect(jsonPath("$.HCS.item.message").value(ErrorCode.CLUB_ACCESS_DENIED.getMessage()))
                .andExpect(jsonPath("$.HCS.status").value(ErrorCode.CLUB_ACCESS_DENIED.getStatus()));

        //올바른 접근
        mockMvc.perform(delete("/club/delete")
                        .param("clubId", club.getId().toString())
                        .param("userEmail", manager.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.item.clubId").value(club.getId()));

        Club deletedClub = clubMapper.findById(club.getId());
        assertNull(deletedClub);

    }

    @DisplayName("Club members - member 등록 요청")
    @Test
    void joinClubAsMember() throws Exception {
        User user = jdbcTemplateHelper.generateTestUser("testUser");
        Club club = jdbcTemplateHelper.generateTestClubBySizeAndCategoryId(1, 1).get(0);
        User manager = jdbcTemplateHelper.generateTestUserAndJoinClub(club, UserType.MANAGER, 1).iterator().next();

        //올바른 요청
        mockMvc.perform(post("/club/members")
                        .param("clubId", club.getId().toString())
                        .param("userEmail", user.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.status").value(200))
                .andExpect(jsonPath("$.HCS.item.member.applicantId").value(user.getId()))
                .andExpect(jsonPath("$.HCS.item.member.currentMembersCount").value(1));

        //잘못된 요청 : 이미 가입한 member
        mockMvc.perform(post("/club/members")
                        .param("clubId", club.getId().toString())
                        .param("userEmail", user.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.item.errorCode").value(ErrorCode.ALREADY_JOINED_CLUB.getErrorCode()))
                .andExpect(jsonPath("$.HCS.item.message").value(ErrorCode.ALREADY_JOINED_CLUB.getMessage()))
                .andExpect(jsonPath("$.HCS.status").value(ErrorCode.ALREADY_JOINED_CLUB.getStatus()));

        //잘못된 요청 : 이미 가입한 manager
        mockMvc.perform(post("/club/members")
                        .param("clubId", club.getId().toString())
                        .param("userEmail", manager.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.item.errorCode").value(ErrorCode.ALREADY_JOINED_CLUB.getErrorCode()))
                .andExpect(jsonPath("$.HCS.item.message").value(ErrorCode.ALREADY_JOINED_CLUB.getMessage()))
                .andExpect(jsonPath("$.HCS.status").value(ErrorCode.ALREADY_JOINED_CLUB.getStatus()));
    }

    @DisplayName("없는 club 요청")
    @Test
    void IllegalArgumentException() throws Exception {
        mockMvc.perform(get("/club/info")
                        .param("clubId", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.item.errorCode").value(ErrorCode.ILLEGAL_ARGUMENT.getErrorCode()))
                .andExpect(jsonPath("$.HCS.item.message").value(ErrorCode.ILLEGAL_ARGUMENT.getMessage()))
                .andExpect(jsonPath("$.HCS.item.location").value("club"))
                .andExpect(jsonPath("$.HCS.status").value(ErrorCode.ILLEGAL_ARGUMENT.getStatus()));

    }

    @DisplayName("Club expulsion members - 멤버 강퇴")
    @Test
    void expulsionMember() throws Exception {
        Club club = jdbcTemplateHelper.generateTestClubBySizeAndCategoryId(1, 1L).get(0);
        User member = jdbcTemplateHelper.generateTestUserAndJoinClub(club, UserType.MEMBER, 1).iterator().next();
        User manager = jdbcTemplateHelper.generateTestUserAndJoinClub(club, UserType.MANAGER, 1).iterator().next();

        //잘못된 입력 :  manager 가 아닌 member user 가 요청할경우 - CLUB_ACCESS_DENIED
        User member2 = jdbcTemplateHelper.generateTestUserAndJoinClub(club, UserType.MEMBER, 1).iterator().next();
        mockMvc.perform(delete("/club/delete/members")
                        .param("clubId", club.getId().toString())
                        .param("managerEmail", member2.getEmail())
                        .param("userId", String.valueOf(member.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.status").value(ErrorCode.CLUB_ACCESS_DENIED.getStatus()))
                .andExpect(jsonPath("$.HCS.item.errorCode").value(ErrorCode.CLUB_ACCESS_DENIED.getErrorCode()))
                .andExpect(jsonPath("$.HCS.item.message").value(ErrorCode.CLUB_ACCESS_DENIED.getMessage()));

        //잘못된 입력 :  member 가 아닌 user 탈퇴를 요청할경우 - NOT_JOINED_CLUB
        User justUser = jdbcTemplateHelper.generateTestUser("justUser");
        mockMvc.perform(delete("/club/delete/members")
                        .param("clubId", club.getId().toString())
                        .param("managerEmail", manager.getEmail())
                        .param("userId", String.valueOf(justUser.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.status").value(ErrorCode.NOT_JOINED_CLUB.getStatus()))
                .andExpect(jsonPath("$.HCS.item.errorCode").value(ErrorCode.NOT_JOINED_CLUB.getErrorCode()))
                .andExpect(jsonPath("$.HCS.item.message").value(ErrorCode.NOT_JOINED_CLUB.getMessage()));

        //바른 입력 : manager 가 member 탈퇴 요청
        int beforeMemberCount = jdbcTemplateHelper.getMemberCountAtClub(club);
        mockMvc.perform(delete("/club/delete/members")
                        .param("clubId", club.getId().toString())
                        .param("managerEmail", manager.getEmail())
                        .param("userId", String.valueOf(member.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect((jsonPath("$.HCS.status").value(200)))
                .andExpect((jsonPath("$.HCS.item.member.firedId").value(member.getId())))
                .andExpect((jsonPath("$.HCS.item.member.currentMembersCount").value(beforeMemberCount - 1)));

        int currentMemberCount = jdbcTemplateHelper.getMemberCountAtClub(club);
        assertEquals(currentMemberCount, beforeMemberCount - 1);

    }

}
