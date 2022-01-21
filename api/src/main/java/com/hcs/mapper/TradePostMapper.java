package com.hcs.mapper;

import com.hcs.domain.TradePost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradePostMapper {

    TradePost findById(long tradePostId);

    TradePost findByTitle(String title);

    Long findAuthorIdById(long tradePostId);

    int countByTitle(String title);

    long insertTradePost(TradePost tradePost);

    int updateTradePost(TradePost tradePost);

    int updateTradePostForView(long tradePostId);

    int deleteTradePostById(long tradePostId);
}
