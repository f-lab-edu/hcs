package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.dto.response.club.ClubInListDto;
import com.hcs.dto.response.tradePost.TradePostListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class HcsList {

    @Autowired
    private ObjectMapper objectMapper;

    public ObjectNode tradePost(TradePostListDto tradePostListDto) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.valueToTree(tradePostListDto);

        hcs.put("status", 200);
        hcs.set("item", item);

        return hcs;
    }

    public ObjectNode club(List<ClubInListDto> clubInListDtos, int page, int count, long totalCount) {
        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.createObjectNode();
        item.put("page", page);
        item.put("count", count);
        item.put("totalCount", totalCount);
        item.put("category", clubInListDtos.get(0).getCategory());

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
            clubNode.remove("category"); //상위 json 에서 사용
            clubNode.put("createdAt", c.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            clubs.add(clubNode);
        }
        return clubs;
    }
}
