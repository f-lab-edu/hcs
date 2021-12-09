package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.mapper.ClubMapper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
        clubDto.setTitle(" 새마을농구동호회"); //앞뒤 공백은 제거됨
        clubDto.setDescription("농구하고싶은사람 모여라");
        clubDto.setCategory("스포츠/농구");
        clubDto.setLocation("Bucheon");
        clubDto.setCreatedAt(LocalDateTime.now());

        MvcResult mvcResult = mockMvc.perform(post("/club/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clubDto))
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn();

        Club newClub = clubMapper.findByTitle("새마을농구동호회");
        assertNotNull(newClub);
        assertNotNull(newClub.getId());
        assertNotNull(newClub.getCategory());
        assertNotNull(newClub.getCreatedAt());
        assertNotNull(newClub.getLocation());
        assertNotNull(newClub.getDescription());
        boolean isMatch = mvcResult.getResponse().getRedirectedUrl().equals("/club/" + newClub.getId());
        assertTrue(isMatch);

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
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk());

        Club club = clubMapper.findByTitle("");
        assertNull(club);

    }

}
