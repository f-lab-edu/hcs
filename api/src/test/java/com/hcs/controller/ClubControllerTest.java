package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.config.EnableMockMvc;
import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.mapper.ClubMapper;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClubMapper clubMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Club 생성 - 입력값 정상")
    @Test
    void createClub_with_correct_input() throws Exception {
        clubDto.setTitle("새마을농구동호회");
        clubDto.setDescription("농구하고싶은사람 모여라");
        clubDto.setCategory("스포츠/농구");
        clubDto.setLocation("Bucheon");
        clubDto.setCreatedAt(LocalDateTime.now());

        mockMvc.perform(post("/club/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubDto))
                        .accept(MediaType.APPLICATION_JSON))
                //.with(csrf())) // security 설정 이후 코드 사용 예정
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.HCS.item.clubId").exists());
    }

    //TODO : exception 추가 후 테스트 수정
//    @DisplayName("Club 생성 - 입력값 오류")
//    @Test
//    void createClub_with_wrong_input() throws Exception {
//        clubDto.setTitle(""); // 빈 타이틀은 허용하지 않음.
//        clubDto.setDescription("농구하고싶은사람 모여라");
//        clubDto.setCategory("스포츠/농구");
//        clubDto.setLocation("Bucheon");
//        clubDto.setCreatedAt(LocalDateTime.now());
//
//        mockMvc.perform(post("/club/submit")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(clubDto))
//                        .accept(MediaType.APPLICATION_JSON))
//                //.with(csrf())) // security 설정 이후 코드 사용 예정
//                .andExpect(status().isOk());
//
//        Club club = clubMapper.findByTitle("");
//        assertNull(club);
//
//    }

    @DisplayName("clubInfo -  클럽 정보 요청시 리턴되는 body schema 확인")
    @Test
    void clubInfo() throws Exception { //TODO :
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .category("test category")
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

        String paramName = mvcResult.getRequest().getParameterNames().nextElement();
        String paramValue = mvcResult.getRequest().getParameter(paramName);
        String requestUrl = mvcResult.getRequest().getRequestURL()
                + "?" + paramName
                + "=" + paramValue; //params
        String responseJsonClubUrl = JsonPath.parse(mvcResult.getResponse().getContentAsString()).read("$.HCS.item.club.clubUrl");
        assertEquals(requestUrl, responseJsonClubUrl);

        //TODO : managers , members 객체 추가 후 테스트 수정

        //TODO : exception 추가 후 잘못된 입력 테스트 수정
//        //when
//        mockMvc.perform(get("/club/" + "randomText") //잘못된 id : NumberFormat이 아닌경우
//                .accept(MediaType.APPLICATION_JSON))
//                //then
//                .andExpect(status().is4xxClientError())
//                .andExpect(jsonPath("message").value("잘못된 club id 값을 넣었습니다."));
//
//        long wrongId = club.getId() + 1;
//        //when
//        mockMvc.perform(get("/club/" + wrongId) //잘못된 id : 존재하지않는 club id 인경우
//                .accept(MediaType.APPLICATION_JSON))
//                //then
//                .andExpect(status().is4xxClientError())
//                .andExpect(jsonPath("message").value("존재하지 않는 club id 값입니다."));

    }

}
