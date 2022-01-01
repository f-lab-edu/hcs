package com.hcs.service;

import com.hcs.domain.ChatRoom;
import com.hcs.domain.User;
import com.hcs.mapper.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomMapper chatRoomMapper;

    // '중고거래' 게시물에서 제공된 "판매자와 채팅" 버튼을 통해 방이 만들어지는 경우
    public ChatRoom createChatRoom(ChatRoom newRoom, User buyer, User seller) {

        newRoom.addMember(buyer);
        newRoom.addMember(seller);

        chatRoomMapper.insertChatRoom(newRoom);

        return newRoom;
    }

    // 자신의 기본 DM창에서 방을 만드는 경우
    public ChatRoom createChatRoom(ChatRoom newRoom, User curUser) {

        newRoom.addMember(curUser);

        chatRoomMapper.insertChatRoom(newRoom);

        return newRoom;
    }
}
