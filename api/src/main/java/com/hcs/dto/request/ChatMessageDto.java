package com.hcs.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageDto {

    private String roomId;
    private long authorId;
    private String message;
}
