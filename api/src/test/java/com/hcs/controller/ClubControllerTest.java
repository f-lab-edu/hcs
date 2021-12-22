package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.config.EnableMockMvc;
import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.mapper.ClubMapper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNull;
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());
    }

    @DisplayName("Club 생성 - 입력값 오류")
    @Test
    void createClub_with_wrong_input() throws Exception {
        clubDto.setTitle(""); // 빈 타이틀은 허용하지 않음.
        clubDto.setDescription("농구하고싶은사람 모여라");
        clubDto.setCategory("스포츠/농구");
        clubDto.setLocation("Bucheon");
        clubDto.setCreatedAt(LocalDateTime.now());

        mockMvc.perform(post("/club/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clubDto))
                .accept(MediaType.APPLICATION_JSON))
                //.with(csrf())) // security 설정 이후 코드 사용 예정
                .andExpect(status().isOk());

        Club club = clubMapper.findByTitle("");
        assertNull(club);

    }

    @DisplayName("Club -  submit get request")
    @Test
    void createClubForm() throws Exception {
        mockMvc.perform(get("/club/submit")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @DisplayName("view club -  올바른 club id, 잘못된 id") //TODO: 여기부터 시작
    @Test
    void viewClub() throws Exception { //TODO :
        Club club = Club.builder()
                .title("testClub")
                .location("Bucheon")
                .category("test category")
                .createdAt(LocalDateTime.now())
                .build();
        clubMapper.insertClub(club);

        //when
        mockMvc.perform(get("/club/" + club.getId()) //올바른 id
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("id").value(club.getId()));

        //when
        mockMvc.perform(get("/club/" + "randomText") //잘못된 id : NumberFormat이 아닌경우
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("잘못된 club id 값을 넣었습니다."));

        long wrongId = club.getId() + 1;
        //when
        mockMvc.perform(get("/club/" + wrongId) //잘못된 id : 존재하지않는 club id 인경우
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("존재하지 않는 club id 값입니다."));

    }

}
