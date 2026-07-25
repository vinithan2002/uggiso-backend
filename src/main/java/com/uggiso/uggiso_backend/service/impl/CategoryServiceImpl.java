package com.uggiso.uggiso_backend.service.impl;

import com.uggiso.uggiso_backend.dto.request.CategoryRequest;
import com.uggiso.uggiso_backend.dto.response.CategoryResponse;
import com.uggiso.uggiso_backend.entity.Category;
import com.uggiso.uggiso_backend.entity.Restaurant;
import com.uggiso.uggiso_backend.repository.CategoryRepository;
import com.uggiso.uggiso_backend.repository.RestaurantRepository;
import com.uggiso.uggiso_backend.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               RestaurantRepository restaurantRepository,
                               ModelMapper modelMapper) {

        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        List<Restaurant> restaurants = restaurantRepository.findAll();

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setActive(request.getActive());

        category.setRestaurant(restaurant);

        Category savedCategory = categoryRepository.save(category);

        CategoryResponse response = modelMapper.map(savedCategory, CategoryResponse.class);

        response.setRestaurantId(restaurant.getId());
        response.setRestaurantName(restaurant.getName());

        return response;
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        CategoryResponse response =
                modelMapper.map(category, CategoryResponse.class);

        response.setRestaurantId(category.getRestaurant().getId());
        response.setRestaurantName(category.getRestaurant().getName());

        return response;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(category -> {

                    CategoryResponse response =
                            modelMapper.map(category, CategoryResponse.class);

                    response.setRestaurantId(category.getRestaurant().getId());
                    response.setRestaurantName(category.getRestaurant().getName());

                    return response;

                }).collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getCategoriesByRestaurant(Long restaurantId) {

        return categoryRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(category -> {

                    CategoryResponse response =
                            modelMapper.map(category, CategoryResponse.class);

                    response.setRestaurantId(category.getRestaurant().getId());
                    response.setRestaurantName(category.getRestaurant().getName());

                    return response;

                }).collect(Collectors.toList());
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Manual Mapping
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setActive(request.getActive());

        category.setRestaurant(restaurant);

        Category updatedCategory = categoryRepository.save(category);

        CategoryResponse response = modelMapper.map(updatedCategory, CategoryResponse.class);

        response.setRestaurantId(restaurant.getId());
        response.setRestaurantName(restaurant.getName());

        return response;
    }

    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }
}