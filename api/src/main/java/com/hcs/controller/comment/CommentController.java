package com.hcs.controller.comment;

import com.hcs.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/post/tradePost/")
@RequiredArgsConstructor
public class CommentController {

    @PostMapping("/{id}/comment/add")
    public void addComment(@Valid @RequestBody CommentDto commentDto, HttpServletResponse response,
                           Model model) throws IOException {
        // TODO TradePost글에 댓글을 남기는 작업.
        response.sendRedirect("/post/tradePost/{id}");
    }
}
