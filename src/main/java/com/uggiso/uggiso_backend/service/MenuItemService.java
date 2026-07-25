package com.uggiso.uggiso_backend.service;

import com.uggiso.uggiso_backend.dto.request.MenuItemRequest;
import com.uggiso.uggiso_backend.dto.response.MenuItemResponse;

import java.util.List;

public interface MenuItemService {

    MenuItemResponse createMenuItem(MenuItemRequest request);

    MenuItemResponse getMenuItemById(Long id);

    List<MenuItemResponse> getAllMenuItems();

    List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId);

    List<MenuItemResponse> getAvailableMenuItemsByRestaurant(Long restaurantId);

    List<MenuItemResponse> getMenuItemsByCategory(Long categoryId);

    MenuItemResponse updateMenuItem(Long id, MenuItemRequest request);

    void deleteMenuItem(Long id);

}