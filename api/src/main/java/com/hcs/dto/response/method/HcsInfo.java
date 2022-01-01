package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.domain.Club;
import com.hcs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HcsInfo {

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode profile(User user) {
        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode profile = objectMapper.valueToTree(user);

        item.put("userId", user.getId());

        item.set("profile", profile);

        return item;
    }

    private ObjectNode clubInfo(Club club, String baseUrl) {
        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode clubNode = objectMapper.valueToTree(club);
        clubNode.put("clubUrl", baseUrl + "club/" + club.getId());

        //TODO : managers ,members 객체 추가

        item.put("clubId", club.getId());
        item.set("club", clubNode);
        return item;
    }

    public ObjectNode user(User user) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = profile(user);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode club(Club club, String baseUrl) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = clubInfo(club, baseUrl);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }
}