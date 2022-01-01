package com.hcs.service;

import com.hcs.domain.TradePost;
import com.hcs.dto.TradePostDto;
import com.hcs.mapper.TradePostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradePostService {

    private final TradePostMapper tradePostMapper;

    public boolean saveTradePost(TradePostDto tradePostDto) {
        // TODO 중고게시글 저장하기
        return true;
    }

    public TradePost findByTitle(String title) {
        return tradePostMapper.findByTitle(title);
    }

}
