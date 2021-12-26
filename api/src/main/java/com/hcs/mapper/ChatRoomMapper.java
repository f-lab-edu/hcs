package com.hcs.mapper;

import com.hcs.domain.ChatRoom;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatRoomMapper {
    long insertChatRoom(ChatRoom chatRoom);
}
