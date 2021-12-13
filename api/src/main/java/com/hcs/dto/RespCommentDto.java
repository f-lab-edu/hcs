package com.hcs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespCommentDto {

    private long commentId;
    private boolean success;
}
