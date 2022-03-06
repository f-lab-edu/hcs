package com.hcs.service;

import com.hcs.domain.ChatMessage;
import com.hcs.domain.ChatRoom;
import com.hcs.domain.User;
import com.hcs.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {

    @InjectMocks
    ChatRoomService chatRoomService;

    @Mock
    ChatRoomRepository chatRoomRepository;

    User fixtureUser;
    User fixtureUser_2;
    ChatRoom fixtureChatRoom;
    ChatMessage fixtureChatMessage;

    @BeforeEach
    void init() {

        fixtureUser = User.builder()
                .id(10L)
                .email("testuser@test.com")
                .nickname("testuser")
                .password("testPass")
                .build();
        fixtureUser_2 = User.builder()
                .id(11L)
                .email("testuser2@test.com")
                .nickname("testuser2")
                .password("testPass")
                .build();
        fixtureChatRoom = ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .chatRoomMembers(new HashSet<>())
                .lastChatMesg(null)
                .createdAt(LocalDateTime.now())
                .build();
        fixtureChatMessage = ChatMessage.builder()
                .id(100L)
                .roomId(fixtureChatRoom.getId())
                .authorId(fixtureUser.getId())
                .message("last message")
                .build();
    }

    @DisplayName("chatroom 생성하기")
    @Test
    void createChatRoomForPersonal() {

        given(chatRoomRepository.save(fixtureChatRoom)).willReturn(fixtureChatRoom);

        ChatRoom newRoom = chatRoomService.createChatRoomForPersonal(fixtureChatRoom, fixtureUser, fixtureUser_2);

        assertEquals(newRoom, fixtureChatRoom);
    }

    @DisplayName("chatroom list 내려주기")
    @Test
    void findChatRoomsWithPaging() {

        int page = 1;

        List<ChatRoom> lists = List.of(fixtureChatRoom);
        Page<ChatRoom> list = new PageImpl(lists);
        when(chatRoomRepository.findListsByChatRoomMembersId(anyLong(), any())).thenReturn(list);

        List<ChatRoom> result = chatRoomService.findChatRoomsWithPaging(page, fixtureUser.getId());

        assertEquals(result, lists);
    }

    @DisplayName("chatroom의 가장 마지막으로 주고받은 chatMessge 수정하기")
    @Test
    void updateLastChatMesg() {

        when(chatRoomRepository.findById(fixtureChatRoom.getId())).thenReturn(Optional.of(fixtureChatRoom));

        chatRoomService.updateLastChatMesg(fixtureChatRoom.getId(), fixtureChatMessage);

        assertEquals(fixtureChatRoom.getLastChatMesg(), fixtureChatMessage);
    }
}
