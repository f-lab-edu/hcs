package com.hcs.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;

@Component
public class JdbcTemplateHelper {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long insertTestUser(String newEmail, String newNickname, String newPassword) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String insertSql = "insert into User (email, nickname, password)\n" +
                "values (?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newEmail);
            ps.setString(2, newNickname);
            ps.setString(3, newPassword);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public long insertTestTradePost(long authorId, String title, String productStatus, String category, String description, int price, int salesStatus, LocalDateTime registerationTime) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String insertSql = "insert into TradePost (authorId, title, productStatus, category, description, price, salesStatus, registerationTime)\n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, String.valueOf(authorId));
            ps.setString(2, title);
            ps.setString(3, productStatus);
            ps.setString(4, category);
            ps.setString(5, description);
            ps.setString(6, String.valueOf(price));
            ps.setString(7, String.valueOf(salesStatus));
            ps.setString(8, String.valueOf(registerationTime));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
