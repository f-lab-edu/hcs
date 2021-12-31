package com.hcs.controller;

import com.hcs.dto.ChatMessageDto;
import com.hcs.dto.response.HcsResponseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final HcsResponseManager hcsResponseManager;
//    private final ChatMessageService chatMessageService; // 생성한 이후에 활성화 할 것

    @MessageMapping("/chat/message") // pub
    public void message(ChatMessageDto message) {

        // to subscriber
        template.convertAndSend("/sub/chat/room?roomId=" + message.getRoomId(), message);

//        ChatMessage newChat  = chatMessageService.createChatMessage(message);

        // chatRoom의 chatMessages(Set자료구조)에 현재 글쓴 메시지 저장, members에 자신 추가
        // /chat/room?roomId={roomId}로 redirect

//		return hcsResponseManager.submit.ChatMessage(200, );
    }
}