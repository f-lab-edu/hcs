package com.hcs.service;

import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.CommentDto;
import com.hcs.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final ModelMapper modelMapper;

    public long saveNewComment(@Valid CommentDto commentDto, User user, TradePost tradePost) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setAuthor(user);
        comment.setTradePost(tradePost);
        return commentMapper.insertComment(comment);
    }

    // TODO 대댓글 comment를 DB에 반영하는 함수 작성하기
}
