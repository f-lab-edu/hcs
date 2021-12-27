package com.hcs.controller;

import com.hcs.domain.ChatRoom;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.HcsResponseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    //    private final ChatRoomService chatRoomService; // ChatRoomService 생성 이후로 활성화 할 것임.
    private final SimpMessagingTemplate template;
    private final HcsResponseManager hcsResponseManager;

    @PostMapping("/room/submit")
    public HcsResponse createRoom() {
        ChatRoom newRoom = ChatRoom.create();

//        chatRoomService.createChatRoom(newRoom);

        // 구매자에게 알람 날리기 : 구매자는 알람을 통해 자신의 기본 DM창으로 이동할 수 있음

        return hcsResponseManager.submit.chatRoom(newRoom.getId());
    }

}
