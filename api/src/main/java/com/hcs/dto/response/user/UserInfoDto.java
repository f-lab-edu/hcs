package com.hcs.dto.response.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoDto {

    private long userId;
    private String email;
    private String nickname;

    private boolean emailVerified;
    private LocalDateTime joinedAt;

    private int age;
    private String position;
    private String location;
}
