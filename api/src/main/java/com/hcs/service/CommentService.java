package com.hcs.service;

import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.request.CommentDto;
import com.hcs.exception.global.DatabaseException;
import com.hcs.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final ModelMapper modelMapper;

    public long saveNewComment(CommentDto commentDto, User user, TradePost tradePost) {

        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setAuthor(user);
        comment.setTradePost(tradePost);

        long isSuccess = commentMapper.insertComment(comment);
        if (isSuccess != 1) throw new DatabaseException("DB comment insert");

        return comment.getId();
    }

    public long saveNewReply(CommentDto commentDto, User user, TradePost tradePost, long parentCommentId) {

        Comment reply = modelMapper.map(commentDto, Comment.class);
        reply.setAuthor(user);
        reply.setTradePost(tradePost);
        reply.setParentCommentId(parentCommentId);

        long isSuccess = commentMapper.insertReply(reply);
        if (isSuccess != 1) throw new DatabaseException("DB reply insert");

        return reply.getId();
    }

    public Comment findById(long commentId) {
        return commentMapper.findById(commentId);
    }

    public long modifyComment(long commentId, CommentDto commentDto) {

        Comment comment = findById(commentId);

        String contents = commentDto.getContents();
        comment.setContents(contents);

        int isSuccess = commentMapper.updateComment(comment);

        if (isSuccess != 1) {
            throw new DatabaseException("DB user modify");
        }

        return comment.getId();
    }

    public long deleteCommentById(long commentId) {
        return commentMapper.deleteComment(commentId);
    }
}
