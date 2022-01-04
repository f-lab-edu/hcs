package com.hcs.mapper;

import com.hcs.domain.Comment;
import com.hcs.domain.TradePost;
import com.hcs.domain.User;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @AutoConfigureTestDatabase : 테스트시에 사용될 DB를 별도로 할지 선택할 수 있음
 * @MybatisTest : mybatis의 unit test를 할 경우 사용됨. unit test에 사용될 Bean들만 filtering할 수 있음
 */

@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest(includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[MyBatisConfig]")})
class CommentMapperTest {

    User testUser = new User(); // Dummy 데이터
    TradePost testTradePost = new TradePost();

    @Autowired
    UserMapper userMapper;

    @Autowired
    TradePostMapper tradePostMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    void insertTestUserSQL(String newEmail, String newNickname, String newPassword) {

        String insertSql = "insert into User (email, nickname, password)\n" +
                "values (?, ?, ?)";

        jdbcTemplate.update(insertSql, new Object[]{newEmail, newNickname, newPassword});
    }

    void insertTestTradePostSQL(long authorId, String title, String productStatus, String category, String description, int price, LocalDateTime registerationTime) {

        String insertSql = "insert into TradePost (authorId, title, productStatus, category, description, price, registerationTime)\n" +
                "values (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(insertSql, new Object[]{authorId, title, productStatus, category, description, price, registerationTime});
    }

    void insertTestUserAndTradePost(String userEmail, String tradePostTitle) {

        String newNickname = "test";
        String newPassword = "password";

        insertTestUserSQL(userEmail, newNickname, newPassword);

        long authorId = 43;
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        LocalDateTime registrationTime = LocalDateTime.now();

        insertTestTradePostSQL(authorId, tradePostTitle, productStatus, category, description, price, registrationTime);
    }

    long insertTestComment(String contents) {

        String userEmail = "test@naver.com";
        String title = "test";

        insertTestUserAndTradePost(userEmail, title);

        Optional<User> insertedUser = Optional.ofNullable(userMapper.findByEmail(userEmail));
        Optional<TradePost> insertedTradePost = Optional.ofNullable(tradePostMapper.findByTitle(title));

        long authorId = insertedUser.get().getId();
        long tradePostId = insertedTradePost.get().getId();

        String insertSql = "insert into Comment (authorId, tradePostId, contents)\n" +
                "values (?, ?, ?)";

        jdbcTemplate.update(insertSql, new Object[]{authorId, tradePostId, contents});

        return insertedTradePost.get().getId(); // 생성된 comment의 tradePostId를 리턴함
    }


    @DisplayName("CommentMapper - findByTradePostId 테스트")
    @Test
    void findByTradePostIdTest() {

        String contents = "test 댓글내용";

        Comment selectdComment = commentMapper.findByTradePostId(insertTestComment(contents));

        assertThat(selectdComment.getContents()).isEqualTo("test 댓글내용");
    }

    @DisplayName("CommentMapper - findById 테스트")
    @Test
    void findByIdTest() {

        String contents = "test 댓글내용";

        Comment selectdComment = commentMapper.findByTradePostId(insertTestComment(contents));
        Comment comment = commentMapper.findById(selectdComment.getId());

        assertThat(comment.getContents()).isEqualTo("test 댓글내용");
    }

    @DisplayName("CommentMapper - insertComment 테스트")
    @Test
    void insertCommentTest() {

        long authorId = 31L;
        long tradePostId = 8L;

        Comment testComment = makeTestComment(authorId, tradePostId);

        commentMapper.insertComment(testComment);

        Comment insertedComment = commentMapper.findByTradePostId(8L);

        assertThat(insertedComment).isEqualTo(testComment);
    }

    @DisplayName("CommentMapper - deleteComment 테스트")
    @Test
    void deleteCommentTest() {

        long authorId = 31L;
        long tradePostId = 8L;

        Comment testComment = makeTestComment(authorId, tradePostId);

        commentMapper.insertComment(testComment);

        int result = commentMapper.deleteComment(testComment.getId());

        assertTrue(result > 0);
    }

    public Comment makeTestComment(long authorId, long tradePostId) {
        testUser.setId(authorId);
        testTradePost.setId(tradePostId);

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
