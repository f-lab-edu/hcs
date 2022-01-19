package com.hcs.mapper;

import com.hcs.domain.TradePost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradePostMapper {

    TradePost findById(long tradePostId);

    TradePost findByTitle(String title);

    Long findAuthorIdById(long tradePostId);

    long insertTradePost(TradePost tradePost);

    int deleteTradePostById(long tradePostId);

    // TODO 글쓴이의 이름 가져오기
}
