package com.uggiso.uggiso_backend.controller;

import com.uggiso.uggiso_backend.dto.request.OrderRequest;
import com.uggiso.uggiso_backend.dto.request.UpdateOrderStatusRequest;
import com.uggiso.uggiso_backend.dto.response.DashboardResponse;
import com.uggiso.uggiso_backend.dto.response.OrderResponse;
import com.uggiso.uggiso_backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ================= Place Order =================

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest request) {

        return new ResponseEntity<>(
                orderService.placeOrder(request),
                HttpStatus.CREATED
        );
    }

    // ================= Get Order By Id =================

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }

    // ================= Get Orders By User =================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                orderService.getOrdersByUser(userId)
        );
    }

    // ================= Get Orders By Restaurant =================

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByRestaurant(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                orderService.getOrdersByRestaurant(restaurantId)
        );
    }

    // ================= Update Order Status =================

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, request)
        );
    }

    // ================= Cancel Order =================

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId) {

        orderService.cancelOrder(orderId);

        return ResponseEntity.ok("Order cancelled successfully.");
    }
    // ================= Order Dashboard=================
    @GetMapping("/restaurant/{restaurantId}/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(
            @PathVariable Long restaurantId) {

        return ResponseEntity.ok(
                orderService.getDashboard(restaurantId)
        );
    }
}