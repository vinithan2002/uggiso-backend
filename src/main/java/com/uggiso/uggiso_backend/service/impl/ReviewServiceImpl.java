package com.uggiso.uggiso_backend.service.impl;

import com.uggiso.uggiso_backend.dto.request.ReviewRequest;
import com.uggiso.uggiso_backend.dto.response.ReviewResponse;
import com.uggiso.uggiso_backend.entity.Restaurant;
import com.uggiso.uggiso_backend.entity.Review;
import com.uggiso.uggiso_backend.entity.Users;
import com.uggiso.uggiso_backend.repository.RestaurantRepository;
import com.uggiso.uggiso_backend.repository.ReviewRepository;
import com.uggiso.uggiso_backend.repository.UserDetailsRepository;
import com.uggiso.uggiso_backend.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserDetailsRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            UserDetailsRepository userRepository,
            RestaurantRepository restaurantRepository) {

        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public ReviewResponse addReview(ReviewRequest request) {

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Review review = new Review();

        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);

        return convertToResponse(savedReview);
    }

    @Override
    public ReviewResponse getReviewById(Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        return convertToResponse(review);
    }

    @Override
    public List<ReviewResponse> getReviewsByRestaurant(Long restaurantId) {

        return reviewRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getReviewsByUser(Long userId) {

        return reviewRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse updateReview(Long id,
                                       ReviewRequest request) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found"));

        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);

        return convertToResponse(updatedReview);
    }

    @Override
    public void deleteReview(Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        reviewRepository.delete(review);
    }

    private ReviewResponse convertToResponse(Review review) {

        ReviewResponse response = new ReviewResponse();

        response.setReviewId(review.getId());

        response.setUserId(review.getUser().getId());
        response.setUserName(review.getUser().getUsername());

        response.setRestaurantId(review.getRestaurant().getId());
        response.setRestaurantName(review.getRestaurant().getName());

        response.setComment(review.getComment());
        response.setRating(review.getRating());

        response.setCreatedAt(review.getCreatedAt());

        return response;
    }
}