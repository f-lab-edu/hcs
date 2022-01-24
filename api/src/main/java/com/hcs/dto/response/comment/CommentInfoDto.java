package com.hcs.dto.response.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentInfoDto {

    private long commentId;
    private long authorId;
    private String contents;
    private LocalDateTime registerationTime;
}
