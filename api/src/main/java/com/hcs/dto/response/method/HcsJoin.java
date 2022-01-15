package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.dto.response.club.ClubJoinDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HcsJoin {

    @Autowired
    ObjectMapper objectMapper;

    public ObjectNode club(ClubJoinDto dto){
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        ObjectNode member = objectMapper.valueToTree(dto);
        item.set("member", member);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }
}
