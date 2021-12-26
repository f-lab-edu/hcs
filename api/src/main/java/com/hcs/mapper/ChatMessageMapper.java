package com.hcs.mapper;

import com.hcs.domain.ChatMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMessageMapper {

    long insertChatMessage(ChatMessage chatMessage);
}
