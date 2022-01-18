package com.hcs.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;

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
}
