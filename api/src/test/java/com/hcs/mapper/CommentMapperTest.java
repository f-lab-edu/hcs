package com.hcs.mapper;

import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @AutoConfigureTestDatabase : 테스트시에 사용될 DB를 별도로 할지 선택할 수 있음
 * @MybatisTest : mybatis의 unit test를 할 경우 사용됨. unit test에 사용될 Bean들만 filtering할 수 있음
 */

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest(includeFilters = {@ComponentScan.Filter(classes = {Configuration.class, Mapper.class, Bean.class})})
class CommentMapperTest {

    User testUser = new User(); // Dummy 데이터
    TradePost testTradePost = new TradePost();

    @Autowired
    private CommentMapper commentMapper;

    @DisplayName("CommentMapper - insert 테스트")
    @Test
    void insertCommentTest() {

        Comment testComment = makeTestComment();

        int commentId = commentMapper.insertComment(testComment);

        assertEquals(commentId, 1L);
    }

    @DisplayName("CommentMapper - select 테스트")
    @Test
    void selectCommentTest() {

        long selectdCommentId = 15L; // unit test 이후 자동 rollback이 적용되므로 DB에 있는 row의 ID를 변수로 선언하였음

        Comment selectdComment = commentMapper.findById(selectdCommentId);

        assertEquals(selectdComment.getId(), selectdCommentId);
    }

    @DisplayName("CommentMapper - delete 테스트")
    @Test
    void deleteCommentTest() {

        Comment testComment = makeTestComment();
        commentMapper.insertComment(testComment);

        int result = commentMapper.deleteComment(testComment.getId());

        assertTrue(result > 0);
    }

    public Comment makeTestComment() {
        testUser.setId(31L);
        testTradePost.setId(1L);

        Comment testComment = Comment.builder()
                .author(testUser)
                .contents("테스트 댓글입니다.")
                .tradePost(testTradePost)
                .replys(null)
                .registerationTime(LocalDateTime.now())
                .build();

        return testComment;
    }

}
