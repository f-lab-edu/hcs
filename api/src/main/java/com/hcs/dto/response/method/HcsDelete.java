package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.dto.response.club.ClubExpulsionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HcsDelete {

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

    public ObjectNode club(long clubId) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        item.put("clubId", clubId);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode expulsion(ClubExpulsionDto dto) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        ObjectNode member = objectMapper.valueToTree(dto);
        item.set("member", member);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }
}
