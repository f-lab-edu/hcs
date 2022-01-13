package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HcsUpdate {

    @Autowired
    private ObjectMapper objectMapper;

    public ObjectNode club(long clubId, String clubUrl) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        item.put("clubId", clubId);
        item.put("clubUrl", clubUrl);

        hcs.put("status", 200);
        hcs.set("item", item);
        return hcs;
    }
}
