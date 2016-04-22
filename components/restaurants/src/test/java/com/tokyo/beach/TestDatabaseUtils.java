package com.tokyo.beach;

import com.tokyo.beach.restaurants.cuisine.NewCuisine;
import com.tokyo.beach.restaurants.pricerange.PriceRange;
import com.tokyo.beach.restaurants.restaurant.NewRestaurant;
import com.tokyo.beach.restaurants.user.UserRegistration;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class TestDatabaseUtils {
    public static DataSource buildDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost/osusume-test");
        return dataSource;
    }

    public static Long insertUserIntoDatabase(
            JdbcTemplate jdbcTemplate,
            UserRegistration userRegistration
    ) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingColumns("email", "password", "name")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("email", userRegistration.getEmail());
        params.put("password", userRegistration.getPassword());
        params.put("name", userRegistration.getName());

        return insert.executeAndReturnKey(params).longValue();
    }

    public static void createDefaultCuisine(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("INSERT INTO cuisine (id, name) " +
                "SELECT 0, 'Not Specified' " +
                "WHERE NOT EXISTS (SELECT id FROM cuisine WHERE id=0)");
    }

    public static void createDefaultPriceRange(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("INSERT INTO price_range (id, range) " +
                "SELECT 0, 'Not Specified' " +
                "WHERE NOT EXISTS (SELECT id FROM price_range WHERE id=0)");
    }

    public static Long insertCuisineIntoDatabase(
            JdbcTemplate jdbcTemplate,
            NewCuisine newCuisine
    ) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cuisine")
                .usingColumns("name")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", newCuisine.getName());

        return insert.executeAndReturnKey(params).longValue();
    }

    public static Long insertPriceRangeIntoDatabase(
            JdbcTemplate jdbcTemplate,
            PriceRange priceRange
    ) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("price_range")
                .usingColumns("range")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("range", priceRange.getRange());

        return insert.executeAndReturnKey(params).longValue();
    }

    public static Long insertRestaurantIntoDatabase(
            JdbcTemplate jdbcTemplate,
            NewRestaurant newRestaurant,
            Long userId
    ) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("restaurant")
                .usingColumns("name", "cuisine_id", "created_by_user_id", "price_range_id")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", newRestaurant.getName());
        params.put("cuisine_id", newRestaurant.getCuisineId());
        params.put("created_by_user_id", userId);
        params.put("price_range_id", newRestaurant.getPriceRangeId());

        return insert.executeAndReturnKey(params).longValue();
    }

    public static void truncateAllTables(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("TRUNCATE TABLE photo_url, restaurant, cuisine, session, users, comment, likes, price_range");
    }
}
