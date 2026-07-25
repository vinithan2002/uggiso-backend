package com.uggiso.uggiso_backend.repository;

import com.uggiso.uggiso_backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // Search restaurant by name
    List<Restaurant> findByNameContainingIgnoreCase(String name);

    // Search restaurant by cuisine
    List<Restaurant> findByCuisineContainingIgnoreCase(String cuisine);

    // Get Veg Restaurants
    List<Restaurant> findByVegOnly(Boolean vegOnly);

    // Get Active Restaurants
    List<Restaurant> findByActive(Boolean active);

    // Rating >= value
    List<Restaurant> findByRatingGreaterThanEqual(Double rating);

    // Search by City
    List<Restaurant> findByCityIgnoreCase(String city);

    // Search by Name and Active
    List<Restaurant> findByNameContainingIgnoreCaseAndActive(
            String name,
            Boolean active
    );

    // Veg Restaurants in City
    List<Restaurant> findByCityIgnoreCaseAndVegOnly(
            String city,
            Boolean vegOnly
    );

    Optional<Restaurant> findByOwnerId(Long ownerId);

    List<Restaurant> findByCuisineIgnoreCase(String cuisine);
}