package com.uggiso.uggiso_backend.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    private Long cartItemId;

    private Long menuItemId;

    private String menuItemName;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subTotal;
}