package com.hcs.service;

import com.hcs.common.JdbcTemplateHelper;
import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.hcs.dto.request.CommentDto;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @ExtendWith : 테스트 클래스가 Mockito를 사용함을 의미함.
 * @Mock : mock 객체를 생성함.
 * @InjectMocks : 생성한 Mock객체를 주입하여 사용할 수 있도록 만든 객체.
 * @BeforeEach : 테스트 케이스 시작 전에 먼저 실행되는 어노테이션.
 */

@SpringBootTest
@EnableEncryptableProperties
@EnableJpaRepositories(basePackages = {"com.hcs.repository"})
@Transactional
class CommentServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    TradePostService tradePostService;

    @Autowired
    CommentService commentService;

    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;

    @Test
    @DisplayName("Comment 추가가 제대로 동작하는지 테스트")
    void saveNewCommentTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        String contents = "test 댓글내용";

        CommentDto commentDto = CommentDto.builder()
                .contents(contents)
                .build();

        User author = userService.findById(authorId);
        TradePost tradePost = tradePostService.findById(tradePostId);

        long newCommentId = commentService.saveNewComment(commentDto, author, tradePost);

        Comment comment = commentService.findById(newCommentId);

        assertThat(comment.getContents()).isEqualTo(contents);
    }

    @Test
    @DisplayName("Reply 추가가 제대로 동작하는지 테스트")
    void saveNewReplyTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        String contents = "test 댓글내용";
        LocalDateTime comment_registerationTime = LocalDateTime.now();

        long parentCommentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents, comment_registerationTime);

        String replyContents = "test 댓글내용_reply";

        CommentDto replyDto = CommentDto.builder()
                .contents(replyContents)
                .build();

        User author = userService.findById(authorId);
        TradePost tradePost = tradePostService.findById(tradePostId);

        long replyId = commentService.saveNewReply(replyDto, author, tradePost, parentCommentId);

        Comment reply = commentService.findById(replyId);

        assertThat(reply.getContents()).isEqualTo(replyContents);
    }

    @Test
    @DisplayName("Comment 수정이 제대로 동작하는지 테스트")
    void modifyCommentTest() {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long authorId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(authorId, title, productStatus, category, description, price, salesStatus, registrationTime);

        String contents = "test 댓글내용";
        LocalDateTime comment_registerationTime = LocalDateTime.now();

        long commentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents, comment_registerationTime);

        String modifiedContents = "modified_" + contents;

        CommentDto commentDtoForModified = CommentDto.builder()
                .contents(modifiedContents)
                .build();

        long modifiedCommentId = commentService.modifyComment(commentId, commentDtoForModified);

        Comment modifiedComment = commentService.findById(modifiedCommentId);

        assertThat(commentId).isEqualTo(modifiedCommentId);
        assertThat(modifiedComment.getContents()).isEqualTo(modifiedContents);
    }
}
