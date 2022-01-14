package com.hcs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs.annotation.EnableMockMvc;
import com.hcs.dto.request.CommentDto;
import com.hcs.exception.ErrorCode;
import com.hcs.mapper.CommentMapper;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc
@EnableEncryptableProperties
@Transactional
public class CommentControllerTest {

    private static CommentDto testCommentDto = new CommentDto();
    private static String ROOT_URL = "/post/tradePost/";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CommentMapper commentMapper;

    @DisplayName("댓글 달기 - commentDto 정상")
    @Test
    void addComment_with_correct_commentDto() throws Exception {

        String contents = "(테스트용) 허용된 길이안으로 작성된 댓글입니다.";

        String tradePostId = "1";

        testCommentDto.setContents(contents);

        MvcResult mvcResult = mockMvc.perform(post(ROOT_URL + "/comment/submit")
                        .param("postId", tradePostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        List<Long> commentIds = commentMapper.findByTradePostId(Long.parseLong(tradePostId)).stream().map(comment -> comment.getId()).collect(toList());

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("postId")).isEqualTo(Integer.parseInt(tradePostId));
        assertThat(item.get("commentId")).isIn(commentIds.stream().map(c -> c.intValue()).collect(toList()));
        assertThat(item.get("isSuccess")).isEqualTo(true);
    }

    @DisplayName("댓글 달기 - commentDto 오류 - 허용된 댓글 길이 초과")
    @Test
    void addComment_with_wrong_commentDto() throws Exception {

        String contents = "(테스트용) 허용된 길이를 초과하는 댓글입니다.".repeat(30);

        String tradePostId = "1";

        testCommentDto.setContents(contents);

        MvcResult mvcResult = mockMvc.perform(post(ROOT_URL + "/comment/submit")
                        .param("postId", tradePostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDto))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        ErrorCode error = ErrorCode.METHOD_ARGUMENT_NOT_VALID;

        assertThat(status).isEqualTo(error.getStatus());
        assertThat(item.get("errorCode")).isEqualTo(error.getErrorCode());
        assertThat(item.get("message")).isEqualTo(error.getMessage());

        String field = JsonPath.parse(response).read("$.HCS.item.errors[0].field");
        String code = JsonPath.parse(response).read("$.HCS.item.errors[0].code");
        String message = JsonPath.parse(response).read("$.HCS.item.errors[0].message");

        assertThat(field).isEqualTo("contents");
        assertThat(code).isEqualTo("Length");
        assertThat(message).isEqualTo("길이가 5에서 200 사이여야 합니다");
    }

}
