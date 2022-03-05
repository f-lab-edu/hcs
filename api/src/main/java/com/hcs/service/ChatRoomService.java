package com.hcs.service;

import com.hcs.domain.ChatMessage;
import com.hcs.domain.ChatRoom;
import com.hcs.domain.User;
import com.hcs.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findById(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow();
    }

    public ChatRoom createChatRoomForPersonal(ChatRoom newRoom, User roomMaker, User guest) {

        newRoom.addMembers(roomMaker, guest);

        return chatRoomRepository.save(newRoom);
    }

    public List<ChatRoom> findChatRoomsWithPaging(int page, long userId) {

        int pagePerCount = 7;

        Sort sort = Sort.by("lastChatMesg.createdAt").descending();
        PageRequest pageRequest = PageRequest.of(page - 1, pagePerCount, sort);

        List<ChatRoom> result = chatRoomRepository.findListsByChatRoomMembersId(userId, pageRequest).getContent();

        return result;
    }

    public void updateLastChatMesg(String roomId, ChatMessage chatMessage) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow();
        chatRoom.setLastChatMesg(chatMessage);

        chatRoomRepository.save(chatRoom);
    }
}
