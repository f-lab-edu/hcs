package com.hcs.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.config.advisor.result.ExceptionResult;
import com.hcs.domain.Club;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class HcsResponseManager {

    @Autowired
    public Info info;

    @Autowired
    public Submit submit;

    @Autowired
    private ObjectMapper objectMapper;

    private HcsResponse makeHcsResponse(ObjectNode hcs) {

        HcsResponse response = HcsResponse.builder()
                .HCS(hcs)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return response;
    }


    public HcsResponse Exception(int status, ExceptionResult exceptionResult) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.valueToTree(exceptionResult);

        hcs.put("status", status);
        hcs.set("item", item);

        return makeHcsResponse(hcs);
    }

    @Component
    public class Info {
        private ObjectNode profile(User user) {
            ObjectNode item = objectMapper.createObjectNode();
            ObjectNode profile = objectMapper.valueToTree(user);

            item.put("userId", user.getId());

            item.set("profile", profile);

            return item;
        }

        public HcsResponse User(User user) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = profile(user);

            hcs.put("status", 200);
            hcs.set("item", item);

            return makeHcsResponse(hcs);
        }

        public HcsResponse club(Club club, String baseUrl) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = clubInfo(club, baseUrl);

            hcs.put("status", 200);
            hcs.set("item", item);

            return makeHcsResponse(hcs);
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
    }

    @Component
    public class Submit {

        public HcsResponse user(long userId) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = objectMapper.createObjectNode();

            hcs.put("status", 200);

            item.put("userId", userId);

            hcs.set("item", item);
            return makeHcsResponse(hcs);
        }

        public HcsResponse club(Long clubId, String baseUrl) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = objectMapper.createObjectNode();

            item.put("clubId", clubId);
            item.put("clubUrl", baseUrl + "club/" + clubId.toString());

            hcs.put("status", 200);
            hcs.set("item", item);
            return makeHcsResponse(hcs);
        }

        public HcsResponse chatRoom(String chatRoomId) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = objectMapper.createObjectNode();

            hcs.put("status", 200);

            item.put("chatRoomId", chatRoomId);

            hcs.set("item", item);
            return makeHcsResponse(hcs);
        }

        public HcsResponse chatMessage(long chatMessageId) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = objectMapper.createObjectNode();

            hcs.put("status", 200);

            item.put("chatRoomId", chatMessageId);

            hcs.set("item", item);
            return makeHcsResponse(hcs);
        }
    }

    @Component
    public class list {
        private ObjectNode item(Map<String, Object> tradePostInfo, List<TradePost> tradePostList) {
            ObjectNode item = objectMapper.createObjectNode();
            ArrayNode tradePosts = objectMapper.createArrayNode();

            item.put("page", (int) tradePostInfo.get("page"));
            item.put("count", (int) tradePostInfo.get("count"));
            item.put("salesStatus", (boolean) tradePostInfo.get("salesStatus"));
            item.put("category", (String) tradePostInfo.get("category"));

            for (TradePost tradePost : tradePostList) {
                ObjectNode tradePostNode = objectMapper.valueToTree(tradePost);
                tradePosts.add(tradePostNode);
            }

            item.set("tradePosts", tradePosts);

            return item;
        }

        public HcsResponse tradePost(Map<String, Object> tradePostInfo, List<TradePost> tradePostList) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = item(tradePostInfo, tradePostList);

            hcs.put("status", 200);
            hcs.set("item", item);
            return makeHcsResponse(hcs);
        }
    }

}
