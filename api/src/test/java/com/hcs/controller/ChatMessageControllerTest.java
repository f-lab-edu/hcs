package com.hcs.controller;

import com.hcs.annotation.EnableMockMvc;
import com.hcs.common.JdbcTemplateHelper;
import com.hcs.common.TestSecurityConfig;
import com.hcs.common.TestWebSocketConfig;
import com.hcs.domain.ChatMessage;
import com.hcs.domain.ChatRoom;
import com.hcs.dto.request.ChatMessageDto;
import com.hcs.repository.ChatRoomRepository;
import com.hcs.service.UserService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(classes = {TestSecurityConfig.class, TestWebSocketConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableMockMvc
@EnableEncryptableProperties
public class ChatMessageControllerTest {

    static final String WEBSOCKET_TOPIC = "/sub/chat/room/";
    static final String WEBSOCKET_DESTINATION = "/pub/chat/message";
    static String WEBSOCKET_URI = null;
    @Autowired
    UserService userService;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    long roomMakerId;
    long guestId;
    String roomId;
    BlockingQueue<Object> blockingQueue;
    WebSocketStompClient stompClient;
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void init() {

        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(transports));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        WEBSOCKET_URI = "ws://localhost:" + port + "/chat/inbox";
    }

    @DisplayName("클라이언트에서 채팅 메시지 전송시 처리")
    @Rollback
    @Test
    void sendChatMessageTest() throws Exception {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        roomMakerId = jdbcTemplateHelper.insertTestUser(newEmail + 0, newNickname + 0, newPassword + 0, joinedAt);
        guestId = jdbcTemplateHelper.insertTestUser(newEmail + 1, newNickname + 1, newPassword + 1, joinedAt.plusSeconds(1));

        roomId = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();

        jdbcTemplateHelper.insertTestChatRoom(roomId, createdAt);
        jdbcTemplateHelper.insertTestChatRoom_Members(roomId, roomMakerId, guestId);

        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .roomId(roomId)
                .authorId(roomMakerId)
                .message("(test) hi i'm room maker")
                .build();

        StompSession session = stompClient
                .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                    }

                    @Override
                    public void handleException(
                            StompSession session,
                            StompCommand command,
                            StompHeaders headers,
                            byte[] payload,
                            Throwable exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC + roomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                log.info("Headers " + headers);
                return ChatMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                log.info("Received message : " + payload.toString());
                blockingQueue.offer((ChatMessage) payload);
            }
        });

        session.send(WEBSOCKET_DESTINATION, chatMessageDto);

        ChatMessage received = (ChatMessage) blockingQueue.poll(1, SECONDS);
        assertEquals(chatMessageDto.getMessage(), received.getMessage());
    }

    @AfterEach
    void deleteChat() {
        log.info("roomId : " + roomId);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow();
        chatRoom.setLastChatMesg(null);

        chatRoomRepository.save(chatRoom);
        chatRoomRepository.delete(chatRoom);
        userService.deleteUserById(roomMakerId);
        userService.deleteUserById(guestId);
    }
}
