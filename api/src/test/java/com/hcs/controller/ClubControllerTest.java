package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.domain.Club;
import com.hcs.dto.request.ClubDto;
import com.hcs.mapper.ClubMapper;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private static ClubDto clubDto = new ClubDto();
    private final String domainUrl = "https://localhost:8443/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClubMapper clubMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${domain.url}")
    private String domainUrl;

    @DisplayName("Club Submit - 입력값 정상")
    @Test
    void createClub_with_correct_input() throws Exception {
        clubDto.setTitle("새마을농구동호회");
        clubDto.setDescription("농구하고싶은사람 모여라");
        clubDto.setCategory("sports");
        clubDto.setLocation("Bucheon");
        clubDto.setCreatedAt(LocalDateTime.now());

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
        List<Club> generatedClubList = generateClubWithCategory(generatedClubSize, givenCategoryId);

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

    private List<Club> generateClubWithCategory(int clubSize, long categoryId) {
        List<Club> clubList = new ArrayList<>();
        for (int i = 0; i < clubSize; i++) {
            Club club = Club.builder().title("testClub_" + i)
                    .createdAt(LocalDateTime.now())
                    .description("this is club for test")
                    .location("Mars")
                    .categoryId(categoryId)
                    .build();
            clubMapper.insertClub(club);
            clubList.add(club);
        }
        return clubList;
    }

}
