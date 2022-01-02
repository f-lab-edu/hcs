package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.domain.Club;
import com.hcs.domain.TradePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HcsList {

    private final String domainUrl = "https://localhost:8443/";

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

    public ObjectNode club(List<Club> clubList, String category, int page, int count, long totalCount) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();
        item.put("page", page);
        item.put("count", count);
        item.put("totalCount", totalCount);
        item.put("category", category);

        ArrayNode clubs = clubs(clubList);
        item.set("clubs", clubs);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }
    private ArrayNode clubs(List<Club> clubList){
        ArrayNode clubs = objectMapper.createArrayNode();
        for (Club c: clubList) {
            ObjectNode clubNode = objectMapper.valueToTree(c);
            clubNode.remove("categoryId");
            clubNode.put("clubId", c.getId());
            clubNode.put("clubUrl",domainUrl+"club/"+c.getId());
            //TODO : member count, manager count 필드 추가
            clubs.add(clubNode);
        }
        return clubs;
    }
}
