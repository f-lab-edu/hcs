package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.domain.Club;
import com.hcs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class HcsInfo {

    private final String domainUrl = "https://localhost:8443/";

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode profile(User user) {
        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode profile = objectMapper.valueToTree(user);

        item.put("userId", user.getId());

        item.set("profile", profile);

        return item;
    }

    private ObjectNode clubInfo(Club club, String category) {
        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode clubNode = objectMapper.createObjectNode();
        clubNode.put("clubUrl", domainUrl + "club/" + club.getId());
        ObjectNode clubObject = objectMapper.valueToTree(club).require();
        clubNode.setAll(clubObject);
        clubNode.put("category", category);
        clubNode.put("createdAt", club.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

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

    public ObjectNode club(Club club, String category) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = clubInfo(club, category);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }
}
