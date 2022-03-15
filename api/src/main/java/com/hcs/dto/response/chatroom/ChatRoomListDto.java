package com.hcs.dto.response.chatroom;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatRoomListDto {

    private int page;
    private int count;
    private long reqUserId;
    private List<ChatRoomInfoDto> chatRooms;
}
