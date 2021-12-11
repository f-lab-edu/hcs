package com.hcs.controller;

import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.CommentDto;
import com.hcs.dto.RespCommentDto;
import com.hcs.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/post/tradePost/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
//    private final UserService userService; // 충분히 구현된 후 사용될 것임
//    private final TradePostService tradePostService;


    @PostMapping("/{id}/comment/add")
    public RespCommentDto addComment(@Valid @RequestBody CommentDto commentDto, @PathVariable("id") Long id) throws IOException {

        User user = User.builder()
                .id(31L)
                .build(); // Dummy 데이터

        TradePost tradePost = TradePost.builder() // Dummy 데이터
                .id(1L)
                .build();

        int commentId = commentService.saveNewComment(commentDto, user, tradePost);
        boolean success = false;

        if (commentId > 0) {
            success = true;
        }

        return new RespCommentDto(commentId, success);
    }
}
