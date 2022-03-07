package com.hcs.service;

import com.hcs.domain.ChatMessage;
import com.hcs.dto.request.ChatMessageDto;
import com.hcs.repository.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {

    @InjectMocks
    ChatMessageService chatMessageService;

    @Mock
    ModelMapper modelMapper;

    @Mock
    ChatMessageRepository chatMessageRepository;

    ChatMessage chatMessage;

    @BeforeEach
    void init() {
        chatMessage = ChatMessage.builder()
                .roomId("testRoom")
                .authorId(100L)
                .message("test message")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @DisplayName("ChatMessage 생성하기")
    @Test
    void createChatMessageTest() {
        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .roomId("testRoom")
                .authorId(100)
                .message("test message")
                .build();

        given(modelMapper.map(chatMessageDto, ChatMessage.class)).willReturn(chatMessage);

        ChatMessage result = chatMessageService.createChatMessage(chatMessageDto);

        assertEquals(result, chatMessage);
    }

    @DisplayName("ChatMessage 리스트 내려주기")
    @Test
    void findChatMessagesWithPagingTest() {

        int page = 1;

        List<ChatMessage> lists = List.of(chatMessage);
        Page<ChatMessage> list = new PageImpl(lists);
        when(chatMessageRepository.findListsByRoomId(anyString(), any())).thenReturn(list);

        List<ChatMessage> result = chatMessageService.findChatMessagesWithPaging(page, chatMessage.getRoomId());

        assertEquals(result, lists);
    }
}
