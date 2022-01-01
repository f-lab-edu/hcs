package com.hcs.service;

import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.request.CommentDto;
import com.hcs.mapper.CommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @ExtendWith : 테스트 클래스가 Mockito를 사용함을 의미함.
 * @Mock : mock 객체를 생성함.
 * @InjectMocks : 생성한 Mock객체를 주입하여 사용할 수 있도록 만든 객체.
 * @BeforeEach : 테스트 케이스 시작 전에 먼저 실행되는 어노테이션.
 */

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentMapper commentMapper;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    CommentService commentService;

    // 테스트에 사용될 Dummy 데이터
    User author;
    TradePost tradePost;
    CommentDto commentDto;

    @BeforeEach
    public void setUp() {
        modelMapper = new ModelMapper();
        commentService = new CommentService(commentMapper, modelMapper);

        author = User.builder().build(); // Dummy 데이터
        tradePost = TradePost.builder().build();
        commentDto = CommentDto.builder().build();
    }

    @Test
    @DisplayName("Comment 추가가 제대로 동작하는지 테스트")
    void testSaveNewComment() {

        // given
        long testAuthorId = 31L;
        long testTradePostId = 1L;
        long newCommentId = 1L;

        String commentContents = "테스트 댓글 입니다.";

        author.setId(testAuthorId);
        tradePost.setId(testTradePostId);
        commentDto.setContents(commentContents);

        Comment comment = Comment.builder()
                .author(author)
                .tradePost(tradePost)
                .build();

        // when
        when(commentMapper.insertComment(comment)).thenReturn(newCommentId);

        // then
        assertThat(commentService.saveNewComment(commentDto, author, tradePost)).isEqualTo(newCommentId);
    }
}
