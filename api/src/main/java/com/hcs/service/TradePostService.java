package com.hcs.service;

import com.hcs.dto.request.TradePostDto;
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
