package com.hcs.dto.response.user;

import lombok.Data;

@Data
public class ChatUserInfoDto {

    private long userId;
    private String nickname;
    private String profileImage;
}
