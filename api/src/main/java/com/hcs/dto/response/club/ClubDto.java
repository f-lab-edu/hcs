package com.hcs.dto.response.club;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClubDto {
    private Long clubId;
    private String clubUrl;
    private String title;
    private String description;
    private String category;
    private String location;
    private LocalDateTime createdAt;
}
