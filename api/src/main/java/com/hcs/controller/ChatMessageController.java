package com.hcs.controller;

import com.hcs.domain.ChatMessage;
import com.hcs.domain.ChatRoom;
import com.hcs.domain.User;
import com.hcs.dto.request.ChatMessageDto;
import com.hcs.service.ChatMessageService;
import com.hcs.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @MessageMapping : 해당 url로 요청이오면 메소드가 실행됨
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate stompTemplate;

    @MessageMapping("/chat/message")
    public void sendChatMessage(ChatMessageDto message) {
        // 실시간으로 방에서 채팅하기
        ChatMessage newChat = chatMessageService.createChatMessage(message);

        log.info("received message : " + message);

        ChatRoom room = chatRoomService.findById(message.getRoomId());
        User friend = room.getChatRoomMembers().stream().filter(m -> m.getId() != message.getAuthorId()).collect(Collectors.toList()).get(0);

        // to chatRoom in subscribers
        stompTemplate.convertAndSend("/sub/messenger/icon/" + friend.getId(), true); // navigation notification
        stompTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), newChat); // chatroom
    }
}
