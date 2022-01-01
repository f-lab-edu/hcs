package com.hcs.controller;

import com.hcs.domain.ChatMessage;
import com.hcs.domain.User;
import com.hcs.dto.ChatMessageDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.HcsResponseManager;
import com.hcs.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * @MessageMapping : 해당 url로 요청이오면 메소드가 실행됨
 */

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final HcsResponseManager hcsResponseManager;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message") // pub
    public HcsResponse message(ChatMessageDto message) {

        User curUser = User.builder()
                .id(31L)
                .build(); // Dummy 데이터

        // to subscriber
        template.convertAndSend("/sub/chat/room?roomId=" + message.getRoomId(), message);

        ChatMessage newChat = chatMessageService.createChatMessage(message);

        return hcsResponseManager.submit.chatMessage(newChat.getId());
    }
}
