package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.domain.TradePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}