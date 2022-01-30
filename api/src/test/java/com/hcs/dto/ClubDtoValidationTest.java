package com.hcs.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.domain.Club;
import com.hcs.dto.request.ClubSubmitDto;
import com.hcs.mapper.ClubMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc
@EnableJpaRepositories(basePackages = {"com.hcs.repository"})
@Transactional
class ClubDtoValidationTest {

    ClubSubmitDto clubDto = new ClubSubmitDto();
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

        mockMvc.perform(post("/club")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clubDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(
                        (result) -> assertTrue(result.getResolvedException().getClass()
                                .isAssignableFrom(MethodArgumentNotValidException.class))
                );

        Club newClub = clubMapper.findByTitle("");
        assertNull(newClub);

    }

}
