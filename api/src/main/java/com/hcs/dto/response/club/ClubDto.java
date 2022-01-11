package com.hcs.dto.response.club;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClubDto {
    private Long clubId;
    private String clubUrl;
    private String title;
    private String description;
    private String location;
    private LocalDateTime createdAt;
}
