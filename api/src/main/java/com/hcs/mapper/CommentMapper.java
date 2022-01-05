package com.hcs.mapper;

import com.hcs.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    Comment findById(long id);

    List<Comment> findByTradePostId(long tradePostId);

    long insertComment(Comment comment);

    int deleteComment(long id);

    // TODO 댓글 수정 추가
}
