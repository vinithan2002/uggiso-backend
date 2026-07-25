package com.uggiso.uggiso_backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequest {

    @NotBlank(message = "Restaurant name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Cuisine is required")
    private String cuisine;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    private String imageUrl;

    @DecimalMin(value = "0.0", message = "Delivery fee must be greater than or equal to 0")
    private BigDecimal deliveryFee;

    @Min(value = 1, message = "Delivery time must be at least 1 minute")
    private Integer deliveryTime;

    @DecimalMin(value = "0.0", message = "Minimum order must be greater than or equal to 0")
    private BigDecimal minimumOrder;

    private Boolean vegOnly;

    private Boolean active;
}