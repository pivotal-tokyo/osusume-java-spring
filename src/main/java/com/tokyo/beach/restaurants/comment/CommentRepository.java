package com.tokyo.beach.restaurants.comment;

import com.tokyo.beach.restaurants.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class CommentRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SerializedComment> findForRestaurant(long restaurantId) {
        return jdbcTemplate.query(
                "SELECT comment.id as comment_id, users.id as user_id, * FROM comment " +
                        "inner join users on comment.created_by_user_id = users.id " +
                        "where restaurant_id = ?" +
                        "order by comment.created_at desc",
                (rs, rowNum) -> new SerializedComment(
                        new Comment(
                                rs.getLong("comment_id"),
                                rs.getString("content"),
                                ZonedDateTime.ofInstant(rs.getTimestamp("created_at").toInstant(), ZoneId.of("UTC")),
                                rs.getLong("restaurant_id"),
                                rs.getLong("created_by_user_id")
                        ),
                        new User(
                                rs.getLong("user_id"),
                                rs.getString("email"),
                                rs.getString("name")
                        )
                ),
                restaurantId
        );

    }
}
