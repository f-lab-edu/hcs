package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HcsSubmit {

    @Autowired
    private ObjectMapper objectMapper;

    public ObjectNode user(long userId) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        hcs.put("status", 200);

        item.put("userId", userId);

        hcs.set("item", item);
        return hcs;
    }

    public ObjectNode club(long clubId, String baseUrl) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        item.put("clubId", clubId);
        item.put("clubUrl", baseUrl + "club/" + clubId);

        hcs.put("status", 200);
        hcs.set("item", item);
        return hcs;
    }

    public ObjectNode chatRoom(String chatRoomId) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        hcs.put("status", 200);

        item.put("chatRoomId", chatRoomId);

        hcs.set("item", item);
        return hcs;
    }

    public ObjectNode chatMessage(long chatMessageId) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        hcs.put("status", 200);

        item.put("chatRoomId", chatMessageId);

        hcs.set("item", item);
        return hcs;
    }
}
