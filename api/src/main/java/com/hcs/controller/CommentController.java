package com.hcs.controller;

import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.RespCommentDto;
import com.hcs.dto.request.CommentDto;
import com.hcs.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/post/tradePost/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
//    private final UserService userService; // 충분히 구현된 후 사용될 것임

    @PostMapping("/comment/submit")
    public RespCommentDto addComment(@Valid @RequestBody CommentDto commentDto, @RequestParam("postId") long tradePostId) throws IOException {

        User user = User.builder()
                .id(31L)
                .build(); // Dummy 데이터

        TradePost tradePost = TradePost.builder()
                .id(tradePostId)
                .build(); // Dummy 데이터

        long commentId = commentService.saveNewComment(commentDto, user, tradePost);
        boolean success = false;

        if (commentId > 0) {
            success = true;
        }

        return new RespCommentDto(commentId, success);
    }
}
