package com.hcs.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.config.advisor.result.ExceptionResult;
import com.hcs.domain.Club;
import com.hcs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HcsResponseManager {

    @Autowired
    public Info info;

    @Autowired
    public Submit submit;

    @Autowired
    private ObjectMapper objectMapper;

    private HcsResponse makeHcsResponse(ObjectNode hcs) {

        HcsResponse response = HcsResponse.builder()
                .HCS(hcs)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return response;
    }

    public HcsResponse Exception(int status, ExceptionResult exceptionResult) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.valueToTree(exceptionResult);

        hcs.put("status", status);
        hcs.set("item", item);

        return makeHcsResponse(hcs);
    }

    @Component
    public class Info {
        private ObjectNode profile(User user) {
            ObjectNode item = objectMapper.createObjectNode();
            ObjectNode profile = objectMapper.valueToTree(user);

            item.put("userId", user.getId());

            item.set("profile", profile);

            return item;
        }

        public HcsResponse User(User user) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = profile(user);

            hcs.put("status", 200);
            hcs.set("item", item);

            return makeHcsResponse(hcs);
        }

        public HcsResponse club(Club club) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = clubInfo(club);

            hcs.put("status", 200);
            hcs.set("item", item);

            return makeHcsResponse(hcs);
        }

        private ObjectNode clubInfo(Club club) {
            ObjectNode item = objectMapper.createObjectNode();
            ObjectNode clubNode = objectMapper.valueToTree(club);

            //TODO : managers ,members 객체 추가

            item.put("clubId", club.getId());
            item.set("club", clubNode);
            return item;
        }

        // TODO 다른 도메인들은 함수를 태워 분기처리하여 item 필드 값을 채움
    }

    @Component
    public class Submit {

        public HcsResponse club(Long clubId) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = objectMapper.createObjectNode();

            item.put("clubId", clubId);
            item.put("clubUrl", "club/Info?clubId=" + clubId.toString()); //TODO : base url 가져오기

            hcs.put("status", 200);
            hcs.set("item", item);
            return makeHcsResponse(hcs);
        }

        public HcsResponse chatRoom(String chatRoomId) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = objectMapper.createObjectNode();

            hcs.put("status", 200);

            item.put("chatRoomId", chatRoomId);

            hcs.set("item", item);
            return makeHcsResponse(hcs);
        }
    }
}
