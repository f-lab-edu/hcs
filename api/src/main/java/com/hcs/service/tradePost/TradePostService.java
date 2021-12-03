package com.hcs.service.tradePost;

import com.hcs.dto.TradePostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class TradePostService {

    @Transactional
    public Long saveTradePost(@Valid TradePostDto tradePostDto) {
        // TODO 중고게시글 저장하기
        return null;
    }

}
