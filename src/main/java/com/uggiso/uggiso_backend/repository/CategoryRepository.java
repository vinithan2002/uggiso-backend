package com.uggiso.uggiso_backend.repository;

import com.uggiso.uggiso_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByRestaurantId(Long restaurantId);

    List<Category> findByNameContainingIgnoreCase(String name);

}