package com.uggiso.uggiso_backend.dto.response;

import   com.uggiso.uggiso_backend.entity.FoodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private String imageUrl;

    private  FoodType foodType;

    private Boolean available;

    private Integer preparationTime;

    private Double rating;

    private Long restaurantId;

    private String restaurantName;

    private Long categoryId;

    private String categoryName;
}