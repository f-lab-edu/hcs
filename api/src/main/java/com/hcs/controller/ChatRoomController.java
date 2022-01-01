package com.hcs.controller;

import com.hcs.domain.ChatRoom;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.HcsResponseManager;
import com.hcs.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final HcsResponseManager hcsResponseManager;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate template;

    @PostMapping("/room/submit")
    public HcsResponse createChatRoom(@RequestParam("userId") User buyer, @RequestParam(name = "tradePostId", required = false) TradePost saleTradePost) {

        ChatRoom newRoom = ChatRoom.create();

        if (saleTradePost == null) {
            chatRoomService.createChatRoom(newRoom, buyer);
        } else {
            chatRoomService.createChatRoom(newRoom, buyer, saleTradePost.getAuthor());
        }

        // 구매자에게 알람 날리기 : 구매자는 알람을 통해 자신의 기본 DM창으로 이동할 수 있음

        return hcsResponseManager.submit.chatRoom(newRoom.getId());
    }

}
