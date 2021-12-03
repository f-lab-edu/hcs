package com.hcs.controller.tradePost;

import com.hcs.domain.TradePost;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post/tradePost")
@RequiredArgsConstructor
public class TradePostController {

    @GetMapping("/")
    public List<TradePost> tradePosts() {
        // TODO 첫 페이지에 보여질 TradePost들을 JSON으로 리턴함.
        return null;
    }


}
