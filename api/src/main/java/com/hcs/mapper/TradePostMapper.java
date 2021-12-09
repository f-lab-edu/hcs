package com.hcs.mapper;

import com.hcs.domain.TradePost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradePostMapper {

    TradePost findByTitle(String title);

    Boolean existsByTitle(String title);

    void save(TradePost tradePost);

    void delete(String title);

    // TODO 글쓴이의 이름 가져오기
}
