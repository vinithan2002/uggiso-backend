package com.uggiso.uggiso_backend.service;

import com.uggiso.uggiso_backend.dto.request.RestaurantRequest;
import com.uggiso.uggiso_backend.dto.response.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse createRestaurant(RestaurantRequest request);

    RestaurantResponse getRestaurantById(Long id);

    List<RestaurantResponse> getAllRestaurants();

    RestaurantResponse updateRestaurant(Long id, RestaurantRequest request);

    void deleteRestaurant(Long id);

    List<RestaurantResponse> searchRestaurant(String keyword);

    List<RestaurantResponse> getVegRestaurants();

    List<RestaurantResponse> getRestaurantsByCuisine(String cuisine);

    RestaurantResponse getMyRestaurant();
}