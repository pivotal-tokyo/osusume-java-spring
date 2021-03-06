package com.tokyo.beach.restaurant;

import com.tokyo.beach.TestDatabaseUtils;
import com.tokyo.beach.restaurants.cuisine.Cuisine;
import com.tokyo.beach.restaurants.pricerange.PriceRange;
import com.tokyo.beach.restaurants.restaurant.NewRestaurant;
import com.tokyo.beach.restaurants.restaurant.Restaurant;
import com.tokyo.beach.restaurants.user.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class RestaurantFixture {
    private long id = 0;
    private String name = "Not Specified";
    private String address = "address";
    private String nearestStation = "Roppongi";
    private String placeId = "place-id";
    private double longitude = 1.23;
    private double latitude = 2.34;
    private String notes = "notes";
    private Cuisine cuisine = new Cuisine(0, "Not Specified");
    private PriceRange priceRange = new PriceRange(0, "Not Specified");
    private User user = new User(0, "email@email", "Not Specified");
    private ZonedDateTime createdAt = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"));
    private ZonedDateTime updatedAt = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("UTC"));


    public RestaurantFixture withId(long id) {
        this.id = id;
        return this;
    }

    public RestaurantFixture withName(String name) {
        this.name = name;
        return this;
    }

    public RestaurantFixture withCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public RestaurantFixture withCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
        return this;
    }

    public RestaurantFixture withPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
        return this;
    }

    public RestaurantFixture withUser(User user) {
        this.user = user;
        return this;
    }

    public RestaurantFixture withUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public RestaurantFixture withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public RestaurantFixture withAddress(String address) {
        this.address = address;
        return this;
    }

    public RestaurantFixture withNearestStation(String nearestStation) {
        this.nearestStation = nearestStation;
        return this;
    }

    public RestaurantFixture withPlaceId(String placeId) {
        this.placeId = placeId;
        return this;
    }

    public RestaurantFixture withLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public RestaurantFixture withLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Restaurant build() {
        return new Restaurant(
                id,
                name,
                address,
                nearestStation,
                placeId,
                latitude,
                longitude,
                notes,
                createdAt,
                updatedAt,
                user.getId(),
                priceRange.getId(),
                cuisine.getId()
        );
    }

    public Restaurant persist(JdbcTemplate jdbcTemplate) {
        NewRestaurantFixture fixture = new NewRestaurantFixture()
                .withName(name)
                .withAddress(address)
                .withNotes(notes);

        if (cuisine != null) {
            fixture = fixture.withCuisineId(cuisine.getId());
        }
        if ( priceRange != null) {
            fixture = fixture.withPriceRangeId(priceRange.getId());
        }
        NewRestaurant newRestaurant = fixture
                .build();

        return TestDatabaseUtils.insertRestaurantIntoDatabase(
                jdbcTemplate,
                newRestaurant,
                user.getId()
        );
    }
}
