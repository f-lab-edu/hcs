package com.hcs.mapper;

import com.hcs.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    Comment findById(long id);

    long insertComment(Comment comment);

    int deleteComment(long id);

    // TODO 댓글 수정 추가
}
