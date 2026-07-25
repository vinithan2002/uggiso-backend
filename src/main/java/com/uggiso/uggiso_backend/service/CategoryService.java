package com.uggiso.uggiso_backend.service;

import com.uggiso.uggiso_backend.dto.request.CategoryRequest;
import com.uggiso.uggiso_backend.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getCategoriesByRestaurant(Long restaurantId);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);

}