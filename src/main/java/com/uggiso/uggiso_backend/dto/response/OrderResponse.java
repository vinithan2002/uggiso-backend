package com.uggiso.uggiso_backend.dto.response;

import com.uggiso.uggiso_backend.entity.OrderStatus;
import com.uggiso.uggiso_backend.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String orderNumber;

    private Long userId;
    private String userName;

    private Long restaurantId;
    private String restaurantName;

    private Long addressId;

    private BigDecimal totalAmount;
    private BigDecimal deliveryCharge;
    private BigDecimal gst;
    private BigDecimal discount;
    private BigDecimal finalAmount;

    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;

    private LocalDateTime orderTime;
    private LocalDateTime deliveryTime;
    private List<OrderItemResponse> orderItems;

}