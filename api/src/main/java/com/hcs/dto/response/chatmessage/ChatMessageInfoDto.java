package com.hcs.dto.response.chatmessage;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageInfoDto {

    private long chatMessageId;
    private long authorId;
    private String message;
    private LocalDateTime createdAt;
}
