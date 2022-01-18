package com.hcs.controller;

import com.hcs.domain.ChatRoom;
import com.hcs.domain.User;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.HcsResponseManager;
import com.hcs.dto.response.method.HcsSubmit;
import com.hcs.service.ChatRoomService;
import com.hcs.service.TradePostService;
import com.hcs.service.UserService;
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

    private final UserService userService;
    private final TradePostService tradePostService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate template;
    private final HcsResponseManager hcsResponseManager;
    private final HcsSubmit submit;

    @PostMapping("/room/submit")
    public HcsResponse createChatRoom(@RequestParam("userId") long userId, @RequestParam(name = "tradePostId", required = false) long saleTradePostId) {

        Long sellerId = tradePostService.findAuthorIdById(saleTradePostId);
        User user = userService.findById(userId);
        User seller = userService.findById(sellerId);

        ChatRoom newRoom = ChatRoom.create();

        if (sellerId == null) {
            chatRoomService.createChatRoom(newRoom, user); // 기본 DM창에서 방을 생성할 경우
            // TODO 초대한 상대방에게 알람 날리기
        } else {
            chatRoomService.createChatRoom(newRoom, user, seller); // 중고거래 게시물의 "판매자와 채팅" 버튼으로 방을 생성할 경우
        }

        // TODO 구매자에게 알람 날리기 : 구매자는 알람을 통해 자신의 기본 DM창으로 이동할 수 있음

        return hcsResponseManager.makeHcsResponse(submit.chatRoom(newRoom.getId()));
    }
}
