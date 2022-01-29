package com.hcs.dto.response.comment;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReplyListDto {

    private int page;
    private int count;
    private long tradePostId;
    private long parentCommentId;
    private List<CommentInfoDto> replys;
}
