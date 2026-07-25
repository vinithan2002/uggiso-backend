package com.uggiso.uggiso_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Long reviewId;

    private Long userId;
    private String userName;

    private Long restaurantId;
    private String restaurantName;

    private String comment;

    private Integer rating;

    private LocalDateTime createdAt;
}