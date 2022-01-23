package com.hcs.common;

import com.hcs.domain.Club;
import com.hcs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

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

    public long insertTestComment(long parentCommentId, long authorId, long tradePostId, String contents) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String insertSql = "insert into Comment (parentCommentId, authorId, tradePostId, contents)\n" +
                "values (?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

            if (parentCommentId == 0) ps.setNull(1, Types.INTEGER);
            else ps.setLong(1, parentCommentId);

            ps.setLong(2, authorId);
            ps.setLong(3, tradePostId);
            ps.setString(4, contents);

            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public long insertTestClub(String title, String location, long categoryId) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String insertSql = "insert into Club (title, createdAt, categoryId, location) \n" +
                "values(?,?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, String.valueOf(LocalDateTime.now()));
            ps.setString(3, String.valueOf(categoryId));
            ps.setString(4, location);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void insertTestClubManagers(long clubId, long managerId) {
        String insertSql = "insert into club_managers (clubId, managerId) \n" +
                "values(?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql);
            ps.setLong(1, clubId);
            ps.setLong(2, managerId);
            return ps;
        });
    }

    public void insertTestClubMembers(long clubId, long memberId) {
        String insertSql = "insert into club_members (clubId, memberId) \n" +
                "values(?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql);
            ps.setLong(1, clubId);
            ps.setLong(2, memberId);
            return ps;
        });
    }

    public Club selectTestClub(long clubId) {
        String selectClub = "select * from Club where id ='" + clubId + "'";
        List<Club> clubList = jdbcTemplate.query(selectClub,
                (rs, rowNum) -> Club.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .categoryId(rs.getLong("categoryId"))
                        .location(rs.getString("location"))
                        .managerCount(rs.getInt("managerCount"))
                        .memberCount(rs.getInt("memberCount"))
                        .build());
        return clubList.get(0);
    }

    public void updateTestClub_managerCount(long clubId, int managerCount) {
        String updateManagerCount = "update Club set managerCount = ? where id = ?";
        jdbcTemplate.update(updateManagerCount, managerCount, clubId);
    }

    public void updateTestClub_memberCount(long clubId, int memberCount) {
        String updateMemberCount = "update Club set memberCount = ? where id = ?";
        jdbcTemplate.update(updateMemberCount, memberCount, clubId);
    }

    public int selectTestManagerCountAtClubManagers(long clubId, long userId) {
        String selectQuery = "select count(*) from club_managers " +
                "where clubId = '" + clubId + "'" +
                "and managerId  ='" + userId + "'";
        return jdbcTemplate.queryForObject(selectQuery, int.class);
    }

    public int selectTestMemberCountAtClubMembers(long clubId, long userId) {
        String selectQuery = "select count(*) from club_members " +
                "where clubId = '" + clubId + "'" +
                "and memberId  ='" + userId + "'";
        return jdbcTemplate.queryForObject(selectQuery, int.class);
    }

    public User selectTestUser(long userId) {
        String selectUser = "select * from User where id ='" + userId + "'";
        List<User> userList = jdbcTemplate.query(selectUser,
                (rs, rowNum) -> User.builder()
                        .id(rs.getLong("id"))
                        .nickname(rs.getString("nickname"))
                        .email(rs.getString("email"))
                        .build());
        return userList.get(0);
    }

    public long selectAllTestClubSize() {
        String selectClub = "select * from Club";
        List<Club> clubList = jdbcTemplate.query(selectClub,
                (rs, rowNum) -> Club.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .categoryId(rs.getLong("categoryId"))
                        .location(rs.getString("location"))
                        .managerCount(rs.getInt("managerCount"))
                        .memberCount(rs.getInt("memberCount"))
                        .build());
        return clubList.size();
    }

    public void deleteTestClub(long clubId) {
        String deleteClub = "delete from Club where id =?";
        jdbcTemplate.update(deleteClub, clubId);
    }

}
