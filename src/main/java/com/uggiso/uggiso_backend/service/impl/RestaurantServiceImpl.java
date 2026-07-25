package com.uggiso.uggiso_backend.service.impl;

import com.uggiso.uggiso_backend.dto.request.RestaurantRequest;
import com.uggiso.uggiso_backend.dto.response.RestaurantResponse;
import com.uggiso.uggiso_backend.entity.Restaurant;
import com.uggiso.uggiso_backend.entity.Users;
import com.uggiso.uggiso_backend.repository.RestaurantRepository;
import com.uggiso.uggiso_backend.service.CurrentUserService;
import com.uggiso.uggiso_backend.service.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final CurrentUserService currentUserService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 ModelMapper modelMapper, CurrentUserService currentUserService) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
        this.currentUserService = currentUserService;
    }

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request) {

        Users owner = currentUserService.getCurrentUser();
        // Prevent one owner from creating multiple restaurants
        if (restaurantRepository.findByOwnerId(owner.getId()).isPresent()) {
            throw new RuntimeException("You already own a restaurant.");
        }
        Restaurant restaurant = modelMapper.map(request, Restaurant.class);
        restaurant.setOwner(owner);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant, RestaurantResponse.class);
    }

    @Override
    public RestaurantResponse getRestaurantById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found with id : " + id));

        return modelMapper.map(restaurant, RestaurantResponse.class);
    }

    @Override
    public List<RestaurantResponse> getAllRestaurants() {

        return restaurantRepository.findAll()
                .stream()
                .map(restaurant ->
                        modelMapper.map(restaurant, RestaurantResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResponse updateRestaurant(Long id,
                                               RestaurantRequest request) {

        Restaurant restaurant = getRestaurantByIdAndValidateOwner(id);

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setAddress(request.getAddress());
        restaurant.setCity(request.getCity());
        restaurant.setState(request.getState());
        restaurant.setPincode(request.getPincode());
        restaurant.setImageUrl(request.getImageUrl());

        restaurant.setDeliveryFee(
                request.getDeliveryFee() != null
                        ? request.getDeliveryFee().doubleValue()
                        : null
        );

        restaurant.setDeliveryTime(request.getDeliveryTime());

        restaurant.setMinimumOrder(
                request.getMinimumOrder() != null
                        ? request.getMinimumOrder().doubleValue()
                        : null
        );

        restaurant.setVegOnly(request.getVegOnly());
        restaurant.setActive(request.getActive());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(updatedRestaurant, RestaurantResponse.class);
    }

    @Override
    public void deleteRestaurant(Long id) {

        Restaurant restaurant = getRestaurantByIdAndValidateOwner(id);

        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<RestaurantResponse> searchRestaurant(String keyword) {

        return restaurantRepository
                .findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(restaurant ->
                        modelMapper.map(restaurant, RestaurantResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantResponse> getVegRestaurants() {

        return restaurantRepository
                .findByVegOnly(true)
                .stream()
                .map(restaurant ->
                        modelMapper.map(restaurant, RestaurantResponse.class))
                .collect(Collectors.toList());
    }

    public List<RestaurantResponse> getRestaurantsByCuisine(String cuisine) {

        List<Restaurant> restaurants =
                restaurantRepository.findByCuisineIgnoreCase(cuisine);

        List<RestaurantResponse> responseList = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {

            RestaurantResponse response = new RestaurantResponse();

            response.setId(restaurant.getId());
            response.setName(restaurant.getName());
            response.setDescription(restaurant.getDescription());
            response.setCuisine(restaurant.getCuisine());
            response.setAddress(restaurant.getAddress());
            response.setCity(restaurant.getCity());
            response.setState(restaurant.getState());
            response.setPincode(restaurant.getPincode());
            response.setImageUrl(restaurant.getImageUrl());
            response.setRating(restaurant.getRating());

            // Convert Double -> BigDecimal
            response.setDeliveryFee(
                    restaurant.getDeliveryFee() != null
                            ? java.math.BigDecimal.valueOf(restaurant.getDeliveryFee())
                            : null
            );

            response.setDeliveryTime(restaurant.getDeliveryTime());

            // Convert Double -> BigDecimal
            response.setMinimumOrder(
                    restaurant.getMinimumOrder() != null
                            ? java.math.BigDecimal.valueOf(restaurant.getMinimumOrder())
                            : null
            );

            response.setVegOnly(restaurant.getVegOnly());
            response.setActive(restaurant.getActive());

            responseList.add(response);
        }

        return responseList;
    }

    private Restaurant getRestaurantByIdAndValidateOwner(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found"));

        Users currentUser = currentUserService.getCurrentUser();

        if (!restaurant.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to access this restaurant.");
        }

        return restaurant;
    }

    @Override
    public RestaurantResponse getMyRestaurant() {

        Users owner = currentUserService.getCurrentUser();

        Restaurant restaurant = restaurantRepository
                .findByOwnerId(owner.getId())
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found"));

        return modelMapper.map(restaurant, RestaurantResponse.class);
    }


}