package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.common.UserType;
import com.hcs.domain.Club;
import com.hcs.domain.User;
import com.hcs.dto.request.ClubSubmitDto;
import com.hcs.exception.ErrorCode;
import com.hcs.mapper.ClubMapper;
import com.hcs.mapper.UserMapper;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    JdbcTemplate jdbcTemplate;

    @Value("${domain.url}")
    private String domainUrl;

    @DisplayName("Club Submit - 입력값 정상")
    @Test
    void createClub_with_correct_input() throws Exception {
        clubDto.setTitle("새마을농구동호회");
        clubDto.setDescription("농구하고싶은사람 모여라");
        clubDto.setCategory("sports");
        clubDto.setLocation("Bucheon");

        mockMvc.perform(post("/club/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.HCS.item.clubId").exists());
    }

    @DisplayName("ClubInfo - 클럽 정보 요청")
    @Test
    void clubInfo() throws Exception { //TODO :
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
        String requestUrl = domainUrl + "club/" + club.getId();
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
        List<Club> generatedClubList = generateClubBySizeAndCategoryId(generatedClubSize, givenCategoryId);

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
        List<Club> clubList = generateClubBySizeAndCategoryId(1, 1L);
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

        mockMvc.perform(post("/club/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("clubId", String.valueOf(clubId))
                        .content(objectMapper.writeValueAsString(clubSubmitDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.HCS.item.clubId").value(clubId));

    }

    @Test
    void deleteClub() throws Exception {
        List<Club> clubList = generateClubBySizeAndCategoryId(1, 1L);
        Club club = clubList.get(0);
        Set<User> userSet = generateUserAndJoinClub(club, UserType.MANAGER, 1);
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

}
