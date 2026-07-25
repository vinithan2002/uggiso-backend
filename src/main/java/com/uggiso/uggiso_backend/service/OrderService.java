package com.uggiso.uggiso_backend.service;

import com.uggiso.uggiso_backend.dto.request.OrderRequest;
import com.uggiso.uggiso_backend.dto.request.UpdateOrderStatusRequest;
import com.uggiso.uggiso_backend.dto.response.OrderResponse;
import com.uggiso.uggiso_backend.dto.response.DashboardResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getOrdersByUser(Long userId);

    List<OrderResponse> getOrdersByRestaurant(Long restaurantId);

    OrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    );

    void cancelOrder(Long orderId);

    DashboardResponse getDashboard(Long restaurantId);
}