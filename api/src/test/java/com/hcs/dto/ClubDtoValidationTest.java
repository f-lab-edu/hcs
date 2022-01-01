package com.hcs.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.domain.Club;
import com.hcs.dto.request.ClubDto;
import com.hcs.mapper.ClubMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClubDtoValidationTest {

    ClubDto clubDto = new ClubDto();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClubMapper clubMapper;

    @Test
    @DisplayName("club dto validation - 잘못된 입력")
    void ClubDto_wrong_input() throws Exception {
        clubDto.setTitle("");
        clubDto.setCategory("sports");
        clubDto.setLocation("Bucheon");
        clubDto.setCreatedAt(LocalDateTime.now());

        mockMvc.perform(post("/club/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clubDto))
                .accept(MediaType.APPLICATION_JSON))
                //.with(csrf())) // security 설정 이후 코드 사용 예정
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        (result) -> assertTrue(result.getResolvedException().getClass()
                                .isAssignableFrom(MethodArgumentNotValidException.class))
                );

        Club newClub = clubMapper.findByTitle("");
        assertNull(newClub);

    }

}
