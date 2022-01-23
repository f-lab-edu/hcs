package com.hcs.controller;

import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.method.HcsInfo;
import com.hcs.dto.response.tradePost.TradePostInfoDto;
import com.hcs.dto.response.user.UserInfoDto;
import com.hcs.service.TradePostService;
import com.hcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TradePostService tradePostService;
    private final HcsInfo info;

    @GetMapping("/info")
    public HcsResponse tradePost(@RequestParam("tradePostId") long tradePostId) {

        long authorId = tradePostService.findAuthorIdById(tradePostId);

        TradePost tradePost = tradePostService.findById(tradePostId);
        User author = userService.findById(authorId);

        TradePostInfoDto tradePostInfoDto = modelMapper.map(tradePost, TradePostInfoDto.class);
        UserInfoDto userInfoDto = modelMapper.map(author, UserInfoDto.class);

        tradePostInfoDto.setAuthor(userInfoDto);

        return HcsResponse.of(info.tradePost(tradePostInfoDto));
    }

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
