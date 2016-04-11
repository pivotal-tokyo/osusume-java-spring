package com.tokyo.beach.comment;

import com.tokyo.beach.application.comment.Comment;
import com.tokyo.beach.application.comment.DatabaseCommentRepository;
import com.tokyo.beach.application.comment.NewComment;
import com.tokyo.beach.application.user.UserRegistration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tokyo.beach.TestDatabaseUtils.buildDataSource;
import static com.tokyo.beach.TestDatabaseUtils.insertUserIntoDatabase;
import static com.tokyo.beach.TestDatabaseUtils.truncateAllTables;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class DatabaseCommentRepositoryTest {
    JdbcTemplate jdbcTemplate;
    DatabaseCommentRepository commentRepository;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(buildDataSource());
        commentRepository = new DatabaseCommentRepository(jdbcTemplate);

        jdbcTemplate.update("INSERT INTO cuisine (id, name) " +
                "SELECT 0, 'Not Specified' " +
                "WHERE NOT EXISTS (SELECT id FROM cuisine WHERE id=0)");
    }

    @After
    public void tearDown() throws Exception {
        truncateAllTables(jdbcTemplate);
    }

    @Test
    public void test_create_createsAComment() throws Exception {
        Date startDate = new Date();

        Number userId = insertUserIntoDatabase(
                jdbcTemplate,
                new UserRegistration("joe@pivotal.io", "password", "Joe")
        );

        long restaurantId = jdbcTemplate.queryForObject(
                "INSERT INTO restaurant (name, created_by_user_id) VALUES " +
                        "('TEST RESTAURANT', ?) RETURNING id",
                (rs, rowNum) -> {
                    return rs.getLong("id");
                },
                userId
        );

        Comment createdComment = commentRepository.create(
                new NewComment("New Comment Content"),
                userId.longValue(),
                String.valueOf(restaurantId)
        );


        Comment actualComment = jdbcTemplate.queryForObject(
                "SELECT * FROM comment WHERE id=?",
                (rs, rowNum) -> {
                    return new Comment(
                            rs.getLong("id"),
                            rs.getString("content"),
                            rs.getString("created_at"),
                            rs.getLong("restaurant_id"),
                            rs.getLong("created_by_user_id")
                    );
                },
                createdComment.getId()
        );

        assertThat(actualComment.getContent(), is("New Comment Content"));
        assertThat(actualComment.getCreatedByUserId(), is(userId.longValue()));
        assertThat(actualComment.getRestaurantId(), is(restaurantId));
    }
}