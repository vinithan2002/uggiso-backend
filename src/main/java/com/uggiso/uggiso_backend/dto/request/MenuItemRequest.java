package com.uggiso.uggiso_backend.dto.request;

import com.uggiso.uggiso_backend.entity.FoodType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {

    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    private BigDecimal price;

    private String imageUrl;

    @NotNull(message = "Food type is required")
    private FoodType foodType;

    private Boolean available;

    @Min(value = 1, message = "Preparation time should be at least 1 minute")
    private Integer preparationTime;

    private Double rating;

    @NotNull(message = "Restaurant Id is required")
    private Long restaurantId;

    @NotNull(message = "Category Id is required")
    private Long categoryId;
}