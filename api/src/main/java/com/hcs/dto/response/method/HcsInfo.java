package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.dto.response.club.ClubInfoDto;
import com.hcs.dto.response.club.ClubUserDto;
import com.hcs.dto.response.comment.CommentInfoDto;
import com.hcs.dto.response.tradePost.TradePostInfoDto;
import com.hcs.dto.response.user.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class HcsInfo {

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectNode profile(UserInfoDto userInfoDto) {

        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode profile = objectMapper.valueToTree(userInfoDto);

        item.set("profile", profile);

        return item;
    }

    private ObjectNode clubInfo(ClubInfoDto clubInfoDto) {
        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode clubNode = objectMapper.createObjectNode();

        ObjectNode clubObject = objectMapper.valueToTree(clubInfoDto);
        clubNode.setAll(clubObject);
        clubNode.remove("clubId"); //상위 json 에서 사용
        clubNode.put("createdAt", clubInfoDto.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (ClubUserDto dto : clubInfoDto.getManagers()) {
            ObjectNode node = objectMapper.valueToTree(dto);
            arrayNode.add(node);
        }
        clubNode.set("managers", arrayNode);

        arrayNode = objectMapper.createArrayNode();
        for (ClubUserDto dto : clubInfoDto.getMembers()) {
            ObjectNode node = objectMapper.valueToTree(dto);
            arrayNode.add(node);
        }
        clubNode.set("members", arrayNode);

        item.put("clubId", clubInfoDto.getClubId());
        item.set("club", clubNode);
        return item;
    }

    public ObjectNode user(UserInfoDto userInfoDto) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = profile(userInfoDto);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode club(ClubInfoDto clubInfoDto) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = clubInfo(clubInfoDto);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode tradePost(TradePostInfoDto tradePostInfoDto) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.valueToTree(tradePostInfoDto);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode comment(long tradePostId, CommentInfoDto commentInfoDto) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode comment = objectMapper.valueToTree(commentInfoDto);

        hcs.put("status", 200);

        item.put("tradePostId", tradePostId);
        item.set("comment", comment);

        hcs.set("item", item);

        return hcs;
    }
}
