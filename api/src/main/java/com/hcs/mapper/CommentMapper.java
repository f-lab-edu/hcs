package com.hcs.mapper;

import com.hcs.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    Comment findById(long id);

    long findAuthorIdById(long id);

    List<Comment> findByTradePostId(long tradePostId);

    List<Comment> findReplysByParentCommentId(long parentCommentId);

    long insertComment(Comment comment);

    long insertReply(Comment comment);

    int updateComment(Comment comment);

    int deleteComment(long id);
}
