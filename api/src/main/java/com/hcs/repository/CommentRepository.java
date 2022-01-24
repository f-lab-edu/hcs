package com.hcs.repository;

import com.hcs.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(value = "Comment.withAuthor", type = EntityGraph.EntityGraphType.LOAD)
    Page<Comment> findCommentsByTradePostId(long tradePostId, Pageable pageable);

    @EntityGraph(value = "Comment.withAuthor", type = EntityGraph.EntityGraphType.LOAD)
    Page<Comment> findReplysByParentCommentId(long parentCommentId, Pageable pageable);
}
