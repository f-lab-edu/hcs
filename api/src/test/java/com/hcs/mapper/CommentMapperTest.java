package com.hcs.mapper;

import com.hcs.common.JdbcTemplateHelper;
import com.hcs.domain.Comment;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @AutoConfigureTestDatabase : 테스트시에 사용될 DB를 별도로 할지 선택할 수 있음
 * @MybatisTest : mybatis의 unit test를 할 경우 사용됨. unit test에 사용될 Bean들만 filtering할 수 있음
 * @DataJpaTest : JPA 관련 테스트 설정만 로드하며 슬라이싱 테스트 시에 사용되는 어노테이션
 */

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = {".*DataSourceConfig", ".*JasyptConfig", ".*Helper"})})
class CommentMapperTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    TradePostMapper tradePostMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;

    @DisplayName("CommentMapper - findById 테스트")
    @Test
    void findByIdTest() {

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

        Comment comment = commentMapper.findById(commentId);

        assertThat(comment.getContents()).isEqualTo(contents);
    }

    @DisplayName("CommentMapper - findByTradePostId 테스트")
    @Test
    void findByTradePostIdTest() {

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

        int lng = 5;

        String contents = "test 댓글내용";
        LocalDateTime comment_registerationTime = LocalDateTime.now();

        for (int i = 0; i < lng; i++) {
            jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, contents + i, comment_registerationTime.plusSeconds(i));
        }

        ArrayList<Comment> comments = (ArrayList<Comment>) commentMapper.findByTradePostId(tradePostId);
        assertThat(comments.size()).isEqualTo(lng);

        Iterator<Comment> it = comments.iterator();

        while (it.hasNext()) {
            Comment comment = commentMapper.findById(it.next().getId());
            assertThat(comment.getContents()).contains(contents);
        }
    }

    @DisplayName("CommentMapper - findReplysByParentCommentId 테스트")
    @Test
    void findReplysByParentCommentIdTest() {

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

        String parentContents = "test 댓글내용";
        LocalDateTime comment_registerationTime = LocalDateTime.now();

        long commentId = jdbcTemplateHelper.insertTestComment(0, authorId, tradePostId, parentContents, comment_registerationTime);

        int lng = 5;

        String contents = "test 댓글내용";

        for (int i = 0; i < lng; i++) {
            jdbcTemplateHelper.insertTestComment(commentId, authorId, tradePostId, contents + i, comment_registerationTime.plusSeconds(i + 1));
        }

        ArrayList<Comment> comments = (ArrayList<Comment>) commentMapper.findReplysByParentCommentId(commentId);
        assertThat(comments.size()).isEqualTo(lng);

        Iterator<Comment> it = comments.iterator();

        while (it.hasNext()) {
            Comment comment = commentMapper.findById(it.next().getId());
            assertThat(comment.getContents()).contains(contents);
        }
    }

    @DisplayName("CommentMapper - insertComment 테스트")
    @Test
    void insertCommentTest() {

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

        String contents = "테스트 댓글입니다.";
        Comment testComment = makeTestComment(authorId, tradePostId, contents);

        commentMapper.insertComment(testComment);

        long commentId = testComment.getId();

        Comment comment = commentMapper.findById(commentId);

        assertThat(comment.getContents()).isEqualTo(contents);
    }

    @DisplayName("CommentMapper - insertReply 테스트")
    @Test
    void insertReplyTest() {

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

        Comment testReply = makeTestReply(parentCommentId, authorId, tradePostId, replyContents);

        commentMapper.insertReply(testReply);

        long commentId = testReply.getId();

        Comment reply = commentMapper.findById(commentId);

        assertThat(reply.getContents()).isEqualTo(replyContents);
    }

    @DisplayName("CommentMapper - updateComment 테스트")
    @Test
    void updateCommentTest() {

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

        Comment comment = commentMapper.findById(commentId);

        String modifiedContents = "modified_" + contents;
        comment.setContents(modifiedContents);

        int isSuccess = commentMapper.updateComment(comment);

        Comment modifiedComment = commentMapper.findById(commentId);

        assertThat(isSuccess).isGreaterThan(0);
        assertThat(modifiedComment.getContents()).isEqualTo(modifiedContents);
    }

    @DisplayName("CommentMapper - deleteComment 테스트")
    @Test
    void deleteCommentTest() {

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

        int result = commentMapper.deleteComment(commentId);

        assertThat(result).isGreaterThan(0);
    }

    private Comment makeTestComment(long authorId, long tradePostId, String contents) {

        User testUser = new User(); // Dummy 데이터

        testUser.setId(authorId);

        Comment testComment = Comment.builder()
                .author(testUser)
                .contents(contents)
                .tradePostId(tradePostId)
                .registerationTime(LocalDateTime.now())
                .build();

        return testComment;
    }

    private Comment makeTestReply(long parentCommentId, long authorId, long tradePostId, String contents) {

        User testUser = new User(); // Dummy 데이터

        testUser.setId(authorId);

        Comment testComment = Comment.builder()
                .parentCommentId(parentCommentId)
                .author(testUser)
                .contents(contents)
                .tradePostId(tradePostId)
                .registerationTime(LocalDateTime.now())
                .build();

        return testComment;
    }
}
