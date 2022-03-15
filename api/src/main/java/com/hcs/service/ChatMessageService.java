package com.hcs.service;

import com.hcs.domain.ChatMessage;
import com.hcs.dto.request.ChatMessageDto;
import com.hcs.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatMessageService {

    private final ModelMapper modelMapper;
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage createChatMessage(ChatMessageDto chatMessageDto) {

        ChatMessage chatMessage = modelMapper.map(chatMessageDto, ChatMessage.class);
        chatMessage.setCreatedAt(LocalDateTime.now());

        log.info("ChatMessageService::chatMessage : " + chatMessage);
        log.info("ChatMessageService::chatMessage roomId : " + chatMessage.getRoomId());

        chatRoomService.updateLastChatMesg(chatMessage.getRoomId(), chatMessage);

        return chatMessage;
    }

    public ChatMessage findById(long chatMessageId) {
        return chatMessageRepository.findById(chatMessageId).orElseThrow();
    }

    public List<ChatMessage> findChatMessagesWithPaging(int page, String roomId) {

        int pagePerCount = 15;

        Sort sort = Sort.by("createdAt").ascending();
        PageRequest pageRequest = PageRequest.of(page - 1, pagePerCount, sort);

        List<ChatMessage> result = chatMessageRepository.findListsByRoomId(roomId, pageRequest).getContent();

        return result;
    }
}
