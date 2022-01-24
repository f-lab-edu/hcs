package com.hcs.controller;

import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.request.CommentDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.comment.CommentInfoDto;
import com.hcs.dto.response.method.HcsInfo;
import com.hcs.dto.response.method.HcsSubmit;
import com.hcs.service.CommentService;
import com.hcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CommentService commentService;
    private final HcsInfo info;
    private final HcsSubmit submit;

    @GetMapping("/comment")
    public HcsResponse commentInfo(@RequestParam("tradePostId") long tradePostId, @RequestParam("commentId") long commentId) {

        Comment comment = commentService.findById(commentId);
        CommentInfoDto commentInfoDto = modelMapper.map(comment, CommentInfoDto.class);

        long authorId = commentService.findAuthorIdById(commentId);
        commentInfoDto.setAuthorId(authorId);

        return HcsResponse.of(info.comment(tradePostId, commentInfoDto));
    }

    @PostMapping("/comment/submit")
    public HcsResponse addComment(@Valid @RequestBody CommentDto commentDto, @RequestParam(value = "parentCommentId", required = false) Long parentCommentId,
                                  @RequestParam("postId") long tradePostId) throws IOException {

        User user = User.builder()
                .id(31L)
                .build(); // Dummy 데이터

        TradePost tradePost = TradePost.builder()
                .id(tradePostId)
                .build(); // Dummy 데이터

        long commentId = commentService.saveNewComment(commentDto, user, tradePost);
        boolean isSuccess = false;

        if (commentId > 0) {
            isSuccess = true;
        }

        if (parentCommentId == null) {
            return HcsResponse.of(submit.comment(tradePostId, commentId, isSuccess));
        }

        return HcsResponse.of(submit.reply(tradePostId, parentCommentId, commentId, isSuccess));
    }
}
