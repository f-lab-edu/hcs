package com.hcs.mapper;

import com.hcs.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    void save(Comment comment);

    void delete(Comment comment);

    // TODO 댓글 수정 추가
}
