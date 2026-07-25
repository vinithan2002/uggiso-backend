package com.uggiso.uggiso_backend.service;

import com.uggiso.uggiso_backend.dto.response.OrderResponse;

import java.util.List;

public interface DeliveryService {

    List<OrderResponse> getAvailableOrders();

    OrderResponse acceptOrder(Long orderId, Long deliveryAgentId);

    List<OrderResponse> getMyOrders(Long deliveryAgentId);

    OrderResponse pickupOrder(Long orderId);

    OrderResponse deliverOrder(Long orderId);

    List<OrderResponse> getHistory(Long deliveryAgentId);
}