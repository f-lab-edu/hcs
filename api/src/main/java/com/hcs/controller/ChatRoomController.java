package com.hcs.controller;

import com.hcs.domain.ChatMessage;
import com.hcs.domain.ChatRoom;
import com.hcs.domain.User;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.chatmessage.ChatMessageInfoDto;
import com.hcs.dto.response.chatroom.ChatRoomInfoDto;
import com.hcs.dto.response.chatroom.ChatRoomListDto;
import com.hcs.dto.response.method.HcsInfo;
import com.hcs.dto.response.method.HcsList;
import com.hcs.dto.response.method.HcsSubmit;
import com.hcs.service.ChatMessageService;
import com.hcs.service.ChatRoomService;
import com.hcs.service.TradePostService;
import com.hcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat/room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TradePostService tradePostService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final HcsInfo info;
    private final HcsList list;
    private final HcsSubmit submit;

    @PostMapping("/personal") // 개인 DM방 생성
    public HcsResponse createPersonalChatRoom(@RequestParam("roomMakerId") long roomMakerId, @RequestParam("guestId") long guestId) {

        User roomMaker = userService.findById(roomMakerId);
        User guest = userService.findById(guestId);

        ChatRoom newRoom = ChatRoom.create();

        newRoom = chatRoomService.createChatRoomForPersonal(newRoom, roomMaker, guest);

        return HcsResponse.of(submit.chatRoom(newRoom.getId(), roomMakerId, guestId));
    }

    @PostMapping("/tradePost") // 중고거래 판매자에게  채팅을 위해 DM방 생성
    public HcsResponse createTradeChatRoom(@RequestParam("buyerId") long buyerId, @RequestParam("tradePostId") long tradePostId) {

        Long sellerId = tradePostService.findAuthorIdById(tradePostId);

        User seller = userService.findById(sellerId);
        User buyer = userService.findById(buyerId);

        ChatRoom newRoom = ChatRoom.create();

        newRoom = chatRoomService.createChatRoomForPersonal(newRoom, seller, buyer);

        return HcsResponse.of(submit.chatRoom(newRoom.getId(), sellerId, buyerId));
    }

    @GetMapping("/")
    public HcsResponse ChatRoomInfo(@RequestParam("roomId") String roomId) {
        // 15개만 채팅내역 보내주기
        ChatRoom chatRoom = chatRoomService.findById(roomId);
        ChatRoomInfoDto chatRoomInfoDto = modelMapper.map(chatRoom, ChatRoomInfoDto.class);

        List<ChatMessage> latestChatMessages = chatMessageService.findChatMessagesWithPaging(1, roomId);
        List<ChatMessageInfoDto> chatMessageInfoDtos = new ArrayList<>();

        for (ChatMessage chatMessage : latestChatMessages) {
            ChatMessageInfoDto info = modelMapper.map(chatMessage, ChatMessageInfoDto.class);

            chatMessageInfoDtos.add(info);
        }

        chatRoomInfoDto.setLatestChatMessages(chatMessageInfoDtos);
        return HcsResponse.of(info.chatRoom(chatRoomInfoDto));
    }

    @GetMapping("/list")
    public HcsResponse getChatRoomList(@RequestParam("userId") long userId) {
        // 채팅방 리스트 7개 전달
        int page = 1;
        List<ChatRoom> chatRooms = chatRoomService.findChatRoomsWithPaging(page, userId);
        List<ChatRoomInfoDto> chatRoomInfoDtos = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomInfoDto info = modelMapper.map(chatRoom, ChatRoomInfoDto.class);
            chatRoomInfoDtos.add(info);
        }

        ChatRoomListDto chatRoomListDto = ChatRoomListDto.builder()
                .page(page)
                .count(chatRooms.size())
                .reqUserId(userId)
                .chatRooms(chatRoomInfoDtos)

                .build();

        return HcsResponse.of(list.chatRoom(chatRoomListDto));
    }
}
