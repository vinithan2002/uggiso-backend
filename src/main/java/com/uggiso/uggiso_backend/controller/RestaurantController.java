package com.uggiso.uggiso_backend.controller;

import com.uggiso.uggiso_backend.dto.request.RestaurantRequest;
import com.uggiso.uggiso_backend.dto.response.RestaurantResponse;
import com.uggiso.uggiso_backend.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "http://localhost:5173")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody RestaurantRequest request) {

        return new ResponseEntity<>(
                restaurantService.createRestaurant(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {

        return ResponseEntity.ok(
                restaurantService.getAllRestaurants()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                restaurantService.getRestaurantById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequest request) {

        return ResponseEntity.ok(
                restaurantService.updateRestaurant(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(
            @PathVariable Long id) {

        restaurantService.deleteRestaurant(id);

        return ResponseEntity.ok("Restaurant deleted successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponse>> searchRestaurant(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                restaurantService.searchRestaurant(keyword)
        );
    }

    @GetMapping("/veg")
    public ResponseEntity<List<RestaurantResponse>> getVegRestaurants() {

        return ResponseEntity.ok(
                restaurantService.getVegRestaurants()
        );
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCuisine(
            @PathVariable String cuisine) {

        return ResponseEntity.ok(
                restaurantService.getRestaurantsByCuisine(cuisine)
        );
    }

    @GetMapping("/my")
    public ResponseEntity<RestaurantResponse> getMyRestaurant() {

        return ResponseEntity.ok(
                restaurantService.getMyRestaurant()
        );
    }
}