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

    public ObjectNode joinClub(ClubJoinDto dto) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        ObjectNode member = objectMapper.valueToTree(dto);
        item.set("member", member);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode tradePost(long authorId, long tradePostId) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        item.put("authorId", authorId);
        item.put("tradePostId", tradePostId);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode comment(long tradePostId, long commentId) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        hcs.put("status", 200);

        item.put("tradePostId", tradePostId);
        item.put("commentId", commentId);

        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode reply(long tradePostId, long parentCommentId, long replyId) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();

        hcs.put("status", 200);

        item.put("tradePostId", tradePostId);
        item.put("parentCommentId", parentCommentId);
        item.put("replyId", replyId);

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

    public ObjectNode addManager(long userId, int managerCount) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode manager = objectMapper.createObjectNode();

        manager.put("applicantId", userId);
        manager.put("currentManagersCount", managerCount);

        hcs.put("status", 200);

        item.put("manager",manager);

        hcs.set("item", item);
        return hcs;
    }
}
