package com.hcs.dto.response.comment;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentListDto {

    private int page;
    private int count;
    private long tradePostId;
    private List<CommentInfoDto> comments;
}
