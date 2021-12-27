package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.domain.Club;
import com.hcs.dto.ClubDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.HcsResponseManager;
import com.hcs.service.ClubService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClubControllerTest {

    @InjectMocks
    ClubController clubController;

    @InjectMocks
    ObjectMapper objectMapper;

    @Mock
    ClubService clubService;

    @Mock
    HcsResponseManager responseManager;

    @Mock
    HcsResponseManager.Submit submit;

    private MockHttpServletRequest request;
    private static Club club1;
    private static ClubDto clubDto;

    @BeforeAll
    static void initFixture() {
        club1 = Club.builder()
                .id(1L)
                .title("test club")
                .description("description")
                .createdAt(LocalDateTime.now())
                .category("test category")
                .location("Earth").build();
        clubDto = new ClubDto(club1.getTitle(),
                club1.getDescription(),
                club1.getCreatedAt(),
                club1.getLocation(),
                club1.getCategory());

    }

    @Test
    void createClub() {

        //given
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ObjectNode hcs = objectMapper.createObjectNode();
        hcs.put("clubId", club1.getId());
        HcsResponse givenResponse = new HcsResponse(hcs, LocalDateTime.now().toString());
        given(clubService.saveNewClub(clubDto)).willReturn(club1);
        given(responseManager.submit.club(club1.getId(), any(String.class))).willReturn(givenResponse); //NullPointerException

        //when
        HcsResponse hcsResponse = clubController.createClub(clubDto, request);

        //then
        assertEquals(hcsResponse.getHCS().findValue("clubId").toString(), club1.getId().toString());
    }

    @Test
    void clubInfo() {
    }
}
