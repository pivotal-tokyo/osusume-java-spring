package com.tokyo.beach.application.cuisine;

import com.tokyo.beach.application.restaurant.Restaurant;
import java.util.List;
import java.util.Optional;

public interface CuisineRepository {
    List<Cuisine> getAll();
    Optional<Cuisine> getCuisine(String id);
    Cuisine createCuisine(NewCuisine newCuisine);
    Cuisine findForRestaurant(Restaurant restaurant);
}