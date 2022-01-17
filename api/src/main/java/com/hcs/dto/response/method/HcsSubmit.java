package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.dto.response.club.ClubJoinDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HcsSubmit {

    private final String domainUrl = "https://localhost:8443/";

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

    public ObjectNode club(long clubId) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        item.put("clubId", clubId);
        item.put("clubUrl", domainUrl + "club/" + clubId);

        hcs.put("status", 200);
        hcs.set("item", item);
        return hcs;
    }

    public ObjectNode joinClub(ClubJoinDto dto){
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        ObjectNode member = objectMapper.valueToTree(dto);
        item.set("member", member);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode comment(long postId, long commentId, boolean isSuccess) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        hcs.put("status", 200);

        item.put("postId", postId);
        item.put("commentId", commentId);
        item.put("isSuccess", isSuccess);

        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode reply(long postId, long parentCommentId, long commentId, boolean isSuccess) {
        ObjectNode hcs = this.comment(postId, commentId, isSuccess);
        ObjectNode item = (ObjectNode) hcs.get("item");

        item.put("parentCommentId", parentCommentId);

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
