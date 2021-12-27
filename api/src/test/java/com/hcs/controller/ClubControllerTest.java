package com.hcs.controller;

import com.hcs.service.ClubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ClubControllerTest {

    @InjectMocks
    ClubController clubController;

    @Mock
    ClubService clubService;


    @Test
    void createClub() {
    }

    @Test
    void clubInfo() {
    }
}
