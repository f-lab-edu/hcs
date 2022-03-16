package com.hcs.controller;

import com.hcs.domain.ChatMessage;
import com.hcs.domain.ChatRoom;
import com.hcs.domain.User;
import com.hcs.dto.request.ChatMessageDto;
import com.hcs.service.ChatMessageService;
import com.hcs.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @MessageMapping : 해당 url로 요청이오면 메소드가 실행됨
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final RabbitTemplate template;

    @MessageMapping("chat.message")
    public void send(ChatMessageDto message) {

        // 실시간으로 방에서 채팅하기
        ChatMessage newChat = chatMessageService.createChatMessage(message);

        log.info("received message : " + message);

        ChatRoom room = chatRoomService.findById(message.getRoomId());
        User friend = room.getChatRoomMembers().stream().filter(m -> m.getId() != message.getAuthorId()).collect(Collectors.toList()).get(0);

        // to chatRoom in subscribers
        template.convertAndSend(CHAT_EXCHANGE_NAME, "icon." + friend.getId(), true);
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + room.getId(), newChat);
    }

    // receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatMessageDto message) {

        log.info("received message : " + message);
    }
}
