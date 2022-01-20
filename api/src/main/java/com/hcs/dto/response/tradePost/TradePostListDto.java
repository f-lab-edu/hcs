package com.hcs.dto.response.tradePost;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TradePostListDto {

    private int page;
    private int count;
    private boolean salesStatus;
    private String category;
    private List<TradePostInfoDto> tradePosts;
}
