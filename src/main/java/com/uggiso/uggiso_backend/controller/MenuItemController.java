package com.uggiso.uggiso_backend.controller;

import com.uggiso.uggiso_backend.dto.request.MenuItemRequest;
import com.uggiso.uggiso_backend.dto.response.MenuItemResponse;
import com.uggiso.uggiso_backend.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@CrossOrigin(origins = "http://localhost:5173")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @Valid @RequestBody MenuItemRequest request) {

        return new ResponseEntity<>(
                menuItemService.createMenuItem(request),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemResponse>> getByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.getAvailableMenuItemsByRestaurant(restaurantId));
    }

    @GetMapping("/owner/{restaurantId}")
    public ResponseEntity<List<MenuItemResponse>> getOwnerMenu(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                menuItemService.getMenuItemsByRestaurant(restaurantId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItemResponse>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByCategory(categoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {

        return ResponseEntity.ok(menuItemService.updateMenuItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {

        menuItemService.deleteMenuItem(id);

        return ResponseEntity.ok("Menu item deleted successfully.");
    }
}