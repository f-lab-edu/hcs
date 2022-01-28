package com.hcs.service;

import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.request.CommentDto;
import com.hcs.exception.global.DatabaseException;
import com.hcs.mapper.CommentMapper;
import com.hcs.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ModelMapper modelMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public long saveNewComment(CommentDto commentDto, User user, TradePost tradePost) {

        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setAuthor(user);
        comment.setTradePostId(tradePost.getId());
        comment.setRegisterationTime(LocalDateTime.now());

        long isSuccess = commentMapper.insertComment(comment);
        if (isSuccess != 1) throw new DatabaseException("DB comment insert");

        return comment.getId();
    }

    public long saveNewReply(CommentDto commentDto, User user, TradePost tradePost, long parentCommentId) {

        Comment reply = modelMapper.map(commentDto, Comment.class);
        reply.setAuthor(user);
        reply.setTradePostId(tradePost.getId());
        reply.setParentCommentId(parentCommentId);
        reply.setRegisterationTime(LocalDateTime.now());

        long isSuccess = commentMapper.insertReply(reply);
        if (isSuccess != 1) throw new DatabaseException("DB reply insert");

        return reply.getId();
    }

    public Comment findById(long commentId) {
        return commentMapper.findById(commentId);
    }

    public long findAuthorIdById(long commentId) {
        return commentMapper.findAuthorIdById(commentId);
    }

    public List<Comment> findCommentsWithPaging(int page, long tradePostId) {

        int pagePerCount = 5;

        PageRequest pageRequest = PageRequest.of(page - 1, pagePerCount, Sort.by("registerationTime").descending());

        List<Comment> result = commentRepository.findListsByTradePostId(tradePostId, pageRequest).getContent();

        return result;
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
