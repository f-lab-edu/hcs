package com.hcs.controller;

import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.request.CommentDto;
import com.hcs.dto.response.HcsResponse;
import com.hcs.dto.response.comment.CommentInfoDto;
import com.hcs.dto.response.comment.CommentListDto;
import com.hcs.dto.response.method.HcsInfo;
import com.hcs.dto.response.method.HcsList;
import com.hcs.dto.response.method.HcsSubmit;
import com.hcs.service.CommentService;
import com.hcs.service.TradePostService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post/tradePost/")
@RequiredArgsConstructor
public class CommentController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TradePostService tradePostService;
    private final CommentService commentService;
    private final HcsInfo info;
    private final HcsSubmit submit;
    private final HcsList list;

    @GetMapping("/comment")
    public HcsResponse commentInfo(@RequestParam("tradePostId") long tradePostId, @RequestParam("commentId") long commentId) {

        Comment comment = commentService.findById(commentId);
        CommentInfoDto commentInfoDto = modelMapper.map(comment, CommentInfoDto.class);

        long authorId = commentService.findAuthorIdById(commentId);
        commentInfoDto.setAuthorId(authorId);

        return HcsResponse.of(info.comment(tradePostId, commentInfoDto));
    }

    @GetMapping("/comment/list")
    public HcsResponse commentsOntheTradePost(@RequestParam("page") int page, @RequestParam("tradePostId") long tradePostId) {

        List<Comment> comments = commentService.findCommentsWithPaging(page, tradePostId);
        List<CommentInfoDto> commentInfoDtos = new ArrayList<>();

        for (Comment comment : comments) {
            CommentInfoDto info = modelMapper.map(comment, CommentInfoDto.class);

            long authorId = commentService.findAuthorIdById(comment.getId());
            info.setAuthorId(authorId);

            commentInfoDtos.add(info);
        }

        CommentListDto commentListDto = CommentListDto.builder()
                .page(page)
                .count(comments.size())
                .tradePostId(tradePostId)
                .comments(commentInfoDtos)
                .build();

        return HcsResponse.of(list.comment(commentListDto));
    }

    @PostMapping("/comment")
    public HcsResponse addComment(@Valid @RequestBody CommentDto commentDto, @RequestParam("authorId") long authorId, @RequestParam("tradePostId") long tradePostId) {

        User user = userService.findById(authorId);
        TradePost tradePost = tradePostService.findById(tradePostId);

        long commentId = commentService.saveNewComment(commentDto, user, tradePost);

        return HcsResponse.of(submit.comment(tradePostId, commentId));
    }

    @PostMapping("/comment/reply")
    public HcsResponse addreplyOnComment(@Valid @RequestBody CommentDto commentDto, @RequestParam("authorId") long authorId, @RequestParam("tradePostId") long tradePostId,
                                         @RequestParam(value = "parentCommentId") long parentCommentId) {

        User user = userService.findById(authorId);
        TradePost tradePost = tradePostService.findById(tradePostId);

        long replyId = commentService.saveNewReply(commentDto, user, tradePost, parentCommentId);

        return HcsResponse.of(submit.reply(tradePostId, parentCommentId, replyId));
    }
}
