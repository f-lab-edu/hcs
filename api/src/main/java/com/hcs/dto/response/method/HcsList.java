package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.domain.TradePost;
import com.hcs.dto.response.club.ClubInListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class HcsList {

    @Autowired
    private ObjectMapper objectMapper;

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

    public ObjectNode tradePost(Map<String, Object> tradePostInfo, List<TradePost> tradePostList) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = item(tradePostInfo, tradePostList);

        hcs.put("status", 200);
        hcs.set("item", item);
        return hcs;
    }

    public ObjectNode club(List<ClubInListDto> clubInListDtos, String category, int page, int count, long totalCount) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();
        item.put("page", page);
        item.put("count", count);
        item.put("totalCount", totalCount);
        item.put("category", category);

        ArrayNode clubs = clubs(clubInListDtos);
        item.set("clubs", clubs);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    private ArrayNode clubs(List<ClubInListDto> clubList) {
        ArrayNode clubs = objectMapper.createArrayNode();
        for (ClubInListDto c : clubList) {
            ObjectNode clubNode = objectMapper.createObjectNode();
            ObjectNode club = objectMapper.valueToTree(c);
            clubNode.setAll(club);
            clubNode.remove("category");
            clubNode.put("createdAt", c.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            clubs.add(clubNode);
        }
        return clubs;
    }
}
