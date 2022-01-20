package com.hcs.controller;

import com.hcs.dto.response.HcsResponse;
import com.hcs.service.TradePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/post/tradePost")
@RequiredArgsConstructor
public class TradePostController {

    private final TradePostService tradePostService;

    @GetMapping("/list")
    public HcsResponse tradePosts(@RequestParam("page") int page, @RequestParam("category") String category, @RequestParam("salesStatus") boolean salesStatus) {

//        List<TradePost> tradePostList = tradePostService.getTradePostList(page, category, salesStatus);

        Map<String, Object> tradePostInfo = new HashMap<>();
        tradePostInfo.put("page", page);
//        tradePostInfo.put("count", tradePostList.size());
        tradePostInfo.put("category", category);
        tradePostInfo.put("salesStatus", salesStatus);

//        return hcsResponseManager.list.tradePost(tradePostInfo, tradePostList);
        return null;
    }

}
