package com.hcs.mapper;

import com.hcs.domain.TradePost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradePostMapper {

    TradePost findById(long tradePostId);

    TradePost findByTitle(String title);

    void insert(TradePost tradePost);

    void deleteById(long Id);

    // TODO 글쓴이의 이름 가져오기
}
