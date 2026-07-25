package com.uggiso.uggiso_backend.service.impl;

import com.uggiso.uggiso_backend.dto.request.MenuItemRequest;
import com.uggiso.uggiso_backend.dto.response.MenuItemResponse;
import com.uggiso.uggiso_backend.entity.Category;
import com.uggiso.uggiso_backend.entity.MenuItem;
import com.uggiso.uggiso_backend.entity.Restaurant;
import com.uggiso.uggiso_backend.repository.CategoryRepository;
import com.uggiso.uggiso_backend.repository.MenuItemRepository;
import com.uggiso.uggiso_backend.repository.RestaurantRepository;
import com.uggiso.uggiso_backend.service.MenuItemService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository,
                               RestaurantRepository restaurantRepository,
                               CategoryRepository categoryRepository,
                               ModelMapper modelMapper) {

        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MenuItemResponse createMenuItem(MenuItemRequest request) {

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        MenuItem menuItem = new MenuItem();

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setFoodType(request.getFoodType());
        menuItem.setAvailable(request.getAvailable());
        menuItem.setPreparationTime(request.getPreparationTime());
        menuItem.setRating(request.getRating());

        menuItem.setRestaurant(restaurant);
        menuItem.setCategory(category);

        MenuItem saved = menuItemRepository.save(menuItem);

        MenuItemResponse response = modelMapper.map(saved, MenuItemResponse.class);

        response.setRestaurantId(restaurant.getId());
        response.setRestaurantName(restaurant.getName());

        response.setCategoryId(category.getId());
        response.setCategoryName(category.getName());

        return response;
    }

    @Override
    public MenuItemResponse getMenuItemById(Long id) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu Item not found"));

        MenuItemResponse response = modelMapper.map(menuItem, MenuItemResponse.class);

        response.setRestaurantId(menuItem.getRestaurant().getId());
        response.setRestaurantName(menuItem.getRestaurant().getName());

        response.setCategoryId(menuItem.getCategory().getId());
        response.setCategoryName(menuItem.getCategory().getName());

        return response;
    }

    @Override
    public List<MenuItemResponse> getAllMenuItems() {

        return menuItemRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId) {

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getAvailableMenuItemsByRestaurant(Long restaurantId) {

        return menuItemRepository
                .findByRestaurantIdAndAvailableTrue(restaurantId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getMenuItemsByCategory(Long categoryId) {

        return menuItemRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu Item not found"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Manual Mapping
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setFoodType(request.getFoodType());
        menuItem.setAvailable(request.getAvailable());
        menuItem.setPreparationTime(request.getPreparationTime());
        menuItem.setRating(request.getRating());

        menuItem.setRestaurant(restaurant);
        menuItem.setCategory(category);

        MenuItem updated = menuItemRepository.save(menuItem);

        return convertToResponse(updated);
    }

    @Override
    public void deleteMenuItem(Long id) {

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu Item not found"));

        menuItem.setAvailable(false);

        menuItemRepository.save(menuItem);
    }

    private MenuItemResponse convertToResponse(MenuItem menuItem) {

        MenuItemResponse response =
                modelMapper.map(menuItem, MenuItemResponse.class);

        response.setRestaurantId(menuItem.getRestaurant().getId());
        response.setRestaurantName(menuItem.getRestaurant().getName());

        response.setCategoryId(menuItem.getCategory().getId());
        response.setCategoryName(menuItem.getCategory().getName());

        return response;
    }
}