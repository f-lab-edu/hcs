package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HcsModify {

    @Autowired
    ObjectMapper objectMapper;

    public ObjectNode user(long userId) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        item.put("userId", userId);

        hcs.put("status", 200);
        hcs.set("item", item);
        return hcs;
    }

    public ObjectNode club(long clubId, String clubUrl) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        item.put("clubId", clubId);
        item.put("clubUrl", clubUrl);

        hcs.put("status", 200);
        hcs.set("item", item);
        return hcs;
    }

    public ObjectNode tradePost(long tradePostId) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        item.put("tradePostId", tradePostId);

        hcs.put("status", 200);
        hcs.set("item", item);
        return hcs;
    }
}
