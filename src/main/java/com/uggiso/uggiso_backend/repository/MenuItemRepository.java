package com.uggiso.uggiso_backend.repository;

import com.uggiso.uggiso_backend.entity.FoodType;
import com.uggiso.uggiso_backend.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId);

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findByCategoryId(Long categoryId);

    List<MenuItem> findByNameContainingIgnoreCase(String name);

    List<MenuItem> findByFoodType(FoodType foodType);

    List<MenuItem> findByAvailable(Boolean available);

    List<MenuItem> findByRestaurantIdAndNameContainingIgnoreCase(
            Long restaurantId,
            String name
    );

    List<MenuItem> findByRestaurantIdAndAvailable(
            Long restaurantId,
            Boolean available
    );

    List<MenuItem> findByRestaurantIdAndFoodType(
            Long restaurantId,
            FoodType foodType
    );
}