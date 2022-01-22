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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JdbcTemplateHelper {

    @Autowired
    JdbcTemplate jdbcTemplate;

    static long idVariable=1;

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

    public User generateTestUser(String name) {
        String email = name + "@test.com";

        String insertSql = "insert into User (email, nickname, password)\n" +
                "values (?, ?, ?)";

        jdbcTemplate.update(insertSql, new Object[]{email, name, name + "pass"});

        String selectUserByEmail = "select * from User where email ='" + email + "'";
        List<User> userList = jdbcTemplate.query(selectUserByEmail,
                (rs, rowNum) -> User.builder()
                        .id(rs.getLong("id"))
                        .nickname(rs.getString("nickname"))
                        .email(rs.getString("email"))
                        .build());
        return userList.get(0);
    }

    public List<Club> generateTestClubBySizeAndCategoryId(int clubSize, long categoryId) {
        String insertSql = "insert into Club (title, createdAt, categoryId, location) \n" +
                "values(?,?,?,?)";
        for (int i = 0; i < clubSize; i++) {
            jdbcTemplate.update(insertSql, new Object[]{"testClub_" + i+idVariable++, LocalDateTime.now(), categoryId, "test location"});

        }
        String selectAllClubs = "select * from Club";
        List<Club> clubList = jdbcTemplate.query(selectAllClubs,
                (rs, rowNum) -> Club.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .categoryId(rs.getLong("categoryId"))
                        .location(rs.getString("location"))
                        .createdAt(LocalDateTime.now())
                        .build()); // id 값을 가져오기위해 재검색
        return clubList;
    }

    public Set<User> generateTestUserAndJoinClub(Club club, UserType userType, int userSize) {
        Set<User> userSet = new HashSet<>();
        String insertSql = "insert into User (email, nickname, password)\n" +
                "values (?, ?, ?)";

        for (int i = 0; i < userSize; i++) {
            String username = "testuser" + i+idVariable++;
            User user = User.builder()
                    .email(username + "@gmail.com")
                    .nickname(username)
                    .password(username + "pass").build();
            jdbcTemplate.update(insertSql, new Object[]{user.getEmail(), user.getNickname(), user.getPassword()});

            String selectUserByEmail = "select * from User where email ='" + user.getEmail() + "'";
            List<User> userList = jdbcTemplate.query(selectUserByEmail,
                    (rs, rowNum) -> User.builder()
                            .id(rs.getLong("id"))
                            .nickname(rs.getString("nickname"))
                            .email(rs.getString("email"))
                            .build());
            User newUser = userList.get(0);
            if (userType == UserType.MANAGER) {
                String insertManager = "insert into club_managers (clubId, managerId) values (?, ?)";
                jdbcTemplate.update(insertManager, new Object[]{club.getId(), newUser.getId()});

            } else if (userType == UserType.MEMBER) {
                String insertMember = "insert into club_members (clubId, memberId) values (?, ?)";
                jdbcTemplate.update(insertMember, new Object[]{club.getId(), newUser.getId()});
            }
            userSet.add(newUser);
        }
        if(userType == UserType.MANAGER){
            int managerCount = getManagerCountAtClub(club);
            String updateManagerCount = "update Club set managerCount = ? where id = ?";
            jdbcTemplate.update(updateManagerCount, managerCount + userSize, club.getId());
        }
        else if(userType == UserType.MEMBER) {
            int memberCount = getMemberCountAtClub(club);
            String updateMemberCount = "update Club set memberCount = ? where id = ?";
            jdbcTemplate.update(updateMemberCount, memberCount + userSize, club.getId());
        }
        return userSet;
    }

    public int getMemberCountAtClub(Club club) {
        String selectMemberCount = "select memberCount from Club where id ='" + club.getId() + "'";
        List<Integer> countList = jdbcTemplate.query(selectMemberCount,
                (rs, rowNum) -> rs.getInt("memberCount"));
        return countList.get(0);
    }
    public int getManagerCountAtClub(Club club) {
        String selectManagerCount = "select managerCount from Club where id ='" + club.getId() + "'";
        List<Integer> countList = jdbcTemplate.query(selectManagerCount,
                (rs, rowNum) -> rs.getInt("managerCount"));
        return countList.get(0);
    }

    public int getMemberCountAtClubMembers(long clubId, long userId) {
        String selectMemberIdByEmail = "select count(*) from club_members " +
                "where clubId = '" + clubId + "'" +
                "and memberId  ='" + userId + "'";
        return jdbcTemplate.queryForObject(selectMemberIdByEmail, int.class);
    }

    public int getManagerCountAtClubManagers(long clubId, long userId) {
        String selectUserByEmail = "select count(*) from club_managers " +
                "where clubId = '" + clubId + "'" +
                "and managerId  ='" + userId + "'";
        return jdbcTemplate.queryForObject(selectUserByEmail, int.class);
    }

    public Club selectClub(long clubId) {
        List<Club> list = jdbcTemplate.query("select * from Club where id=" + clubId
                , (rs, rowNum) -> Club.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .description(rs.getString("description"))
                        .categoryId(rs.getLong("categoryId"))
                        .location(rs.getString("location"))
                        .build());
        return list.get(0);
    }

}
