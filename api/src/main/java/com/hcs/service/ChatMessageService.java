package com.hcs.service;

import com.hcs.domain.ChatMessage;
import com.hcs.dto.ChatMessageDto;
import com.hcs.mapper.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageMapper chatMessageMapper;
    private final ModelMapper modelMapper;

    public ChatMessage createChatMessage(@Valid ChatMessageDto chatMessageDto) {

        ChatMessage newChatMessage = modelMapper.map(chatMessageDto, ChatMessage.class);
        chatMessageMapper.insertChatMessage(newChatMessage);

        return newChatMessage;
    }

}
