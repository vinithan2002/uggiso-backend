package com.uggiso.uggiso_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private Long cartId;

    private Long userId;

    private Long restaurantId;

    private Integer totalItems;

    private BigDecimal totalAmount;

    private List<CartItemResponse> items;
}