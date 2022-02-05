package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    public ObjectNode expulsionMember(long firedId, int currentMembersCount) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        ObjectNode member = objectMapper.createObjectNode();

        member.put("firedId", firedId);
        member.put("currentMembersCount", currentMembersCount);

        item.set("member", member);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode resignMember(long resignedId, int currentMembersCount) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        ObjectNode member = objectMapper.createObjectNode();

        member.put("resignedId", resignedId);
        member.put("currentMembersCount", currentMembersCount);

        item.set("member", member);

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

    public ObjectNode comment(long tradePostId, long commentId) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        item.put("tradePostId", tradePostId);
        item.put("commentId", commentId);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }
}
