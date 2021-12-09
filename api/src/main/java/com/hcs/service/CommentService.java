package com.hcs.service;

import com.hcs.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class CommentService {

    public boolean saveNewComment(@Valid CommentDto commentDto) {
        // TODO TradePost의 댓글로 DB에 반영
        return true;
    }

    // TODO 대댓글 comment를 DB에 반영하는 함수 작성하기
}
