package com.hcs.setting;

import com.hcs.domain.UserForJPA;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @PersistenceContext : EntityManager를 직접 사용하는 경우 주입받을 때 사용되는 어노테이션.
 */

@SpringBootTest
@EnableJpaRepositories(basePackages = {"com.hcs.repository"})
@Transactional
public class JpaWithMybatisTests {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Autowired
    private SqlSession sqlSession;

    @DisplayName("트랜잭션 테스트 - not commit JPA query")
    @Test
    public void test() {
        String testEmail = "test@naver.com";

        UserForJPA jpaUserForJPA = this.make(testEmail);

        em.persist(jpaUserForJPA);

        UserForJPA MybatisUserForJPA = sqlSession.selectOne("com.hcs.mapper.UserMapper.findUserForJpaByEmail", testEmail);

        assertThat(MybatisUserForJPA).isNull();
    }

    @DisplayName("트랜잭션 테스트 - commit JPA query")
    @Test
    public void test2() {
        String testEmail = "test@naver.com";

        UserForJPA jpaUserForJPA = this.make(testEmail);

        em.persist(jpaUserForJPA);
        em.flush();

        UserForJPA MybatisUserForJPA = sqlSession.selectOne("com.hcs.mapper.UserMapper.findUserForJpaByEmail", testEmail);

        assertThat(MybatisUserForJPA).isNotNull();
    }

    public UserForJPA make(String testEmail) {
        String testNickname = "test";
        String testPassword = "password";

        UserForJPA jpaUserForJPA = UserForJPA.builder()
                .email(testEmail)
                .nickname(testNickname)
                .password(testPassword)
                .build();

        return jpaUserForJPA;
    }

}
