package com.hcs.service.tradePost;

import com.hcs.dto.TradePostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class TradePostService {

    public boolean saveTradePost(@Valid TradePostDto tradePostDto) {
        // TODO 중고게시글 저장하기
        return true;
    }

}
