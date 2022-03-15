package com.hcs.dto.response.chatroom;

import com.hcs.domain.ChatMessage;
import com.hcs.dto.response.chatmessage.ChatMessageInfoDto;
import com.hcs.dto.response.user.ChatUserInfoDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ChatRoomInfoDto {

    private String chatRoomId;
    private ChatMessage lastChatMesg;
    private Set<ChatUserInfoDto> chatRoomMembers;
    private List<ChatMessageInfoDto> latestChatMessages;
    private LocalDateTime createdAt;
}
