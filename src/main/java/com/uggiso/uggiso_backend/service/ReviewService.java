package com.uggiso.uggiso_backend.service;

import com.uggiso.uggiso_backend.dto.request.ReviewRequest;
import com.uggiso.uggiso_backend.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse addReview(ReviewRequest request);

    ReviewResponse getReviewById(Long id);

    List<ReviewResponse> getReviewsByRestaurant(Long restaurantId);

    List<ReviewResponse> getReviewsByUser(Long userId);

    ReviewResponse updateReview(Long id, ReviewRequest request);

    void deleteReview(Long id);
}