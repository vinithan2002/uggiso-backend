package com.uggiso.uggiso_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {

    private Long id;

    private String name;

    private String description;

    private String cuisine;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String imageUrl;

    private Double rating;

    private BigDecimal deliveryFee;

    private Integer deliveryTime;

    private BigDecimal minimumOrder;

    private Boolean vegOnly;

    private Boolean active;
}