package com.uggiso.uggiso_backend.service.impl;

import com.uggiso.uggiso_backend.dto.request.OrderRequest;
import com.uggiso.uggiso_backend.dto.request.UpdateOrderStatusRequest;
import com.uggiso.uggiso_backend.dto.response.DashboardResponse;
import com.uggiso.uggiso_backend.dto.response.OrderItemResponse;
import com.uggiso.uggiso_backend.dto.response.OrderResponse;
import com.uggiso.uggiso_backend.entity.*;
import com.uggiso.uggiso_backend.repository.*;
import com.uggiso.uggiso_backend.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserDetailsRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserDetailsRepository userRepository,
            RestaurantRepository restaurantRepository,
            AddressRepository addressRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public OrderResponse placeOrder(OrderRequest request) {

        // ================= Get User =================

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ================= Get Address =================

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // ================= Get Cart =================

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // ================= Get Restaurant From Cart =================

        Restaurant restaurant = cart.getRestaurant();

        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found in cart");
        }

        // ================= Create Order =================

        Orders order = new Orders();

        order.setOrderNumber(
                "ORD-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setAddress(address);

        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setOrderTime(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();

        // ================= Convert Cart Items To Order Items =================

        for (CartItem cartItem : cart.getCartItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setMenuItem(cartItem.getMenuItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());

            BigDecimal subTotal = cartItem.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            orderItem.setSubTotal(subTotal);

            totalAmount = totalAmount.add(subTotal);

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);

        BigDecimal deliveryCharge =
                totalAmount.compareTo(BigDecimal.valueOf(299)) > 0
                        ? BigDecimal.ZERO
                        : BigDecimal.valueOf(40);

        BigDecimal gst = totalAmount.multiply(BigDecimal.valueOf(0.05));

        BigDecimal discount = BigDecimal.ZERO;

        BigDecimal finalAmount = totalAmount
                .add(deliveryCharge)
                .add(gst)
                .subtract(discount);

        order.setDeliveryCharge(deliveryCharge);
        order.setGst(gst);
        order.setDiscount(discount);
        order.setFinalAmount(finalAmount);

        order.setOrderItems(orderItems);

        Orders savedOrder = orderRepository.save(order);

        // ================= Save Order Items =================

        for (OrderItem item : orderItems) {

            item.setOrder(savedOrder);

            orderItemRepository.save(item);

        }

        // ================= Clear Cart =================

        cartItemRepository.deleteAll(cart.getCartItems());

        cart.getCartItems().clear();

        cart.setTotalAmount(BigDecimal.ZERO);

        cart.setTotalItems(0);

        cartRepository.save(cart);

        // ================= Reload Order =================

        savedOrder = orderRepository.findById(savedOrder.getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return convertToResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(Long id) {

        Orders order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id : " + id));

        return convertToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUser(Long userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Orders> orders = orderRepository.findByUserId(user.getId());

        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersByRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found"));

        List<Orders> orders =
                orderRepository.findByRestaurantId(restaurant.getId());

        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        order.setOrderStatus(request.getStatus());

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            order.setDeliveryTime(LocalDateTime.now());
        }

        Orders updatedOrder = orderRepository.save(order);

        return convertToResponse(updatedOrder);
    }

    @Override
    public void cancelOrder(Long orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered order cannot be cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }


    private OrderResponse convertToResponse(Orders order) {

        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getId());
        response.setOrderNumber(order.getOrderNumber());

        response.setUserId(order.getUser().getId());
        response.setUserName(order.getUser().getUsername());

        response.setRestaurantId(order.getRestaurant().getId());
        response.setRestaurantName(order.getRestaurant().getName());

        response.setAddressId(order.getAddress().getId());

        response.setTotalAmount(order.getTotalAmount());
        response.setDeliveryCharge(order.getDeliveryCharge());
        response.setGst(order.getGst());
        response.setDiscount(order.getDiscount());
        response.setFinalAmount(order.getFinalAmount());

        response.setOrderStatus(order.getOrderStatus());
        response.setPaymentMethod(order.getPaymentMethod());

        response.setOrderTime(order.getOrderTime());
        response.setDeliveryTime(order.getDeliveryTime());

        List<OrderItemResponse> itemResponses =
                order.getOrderItems()
                        .stream()
                        .map(this::convertOrderItemResponse)
                        .collect(Collectors.toList());

        response.setOrderItems(itemResponses);

        return response;
    }

    @Override
    public DashboardResponse getDashboard(Long restaurantId) {

        DashboardResponse response = new DashboardResponse();

        response.setTotalOrders(
                orderRepository.countByRestaurantId(restaurantId)
        );

        response.setPendingOrders(
                orderRepository.countByRestaurantIdAndOrderStatus(
                        restaurantId,
                        OrderStatus.PENDING
                )
        );

        response.setCompletedOrders(
                orderRepository.countByRestaurantIdAndOrderStatus(
                        restaurantId,
                        OrderStatus.DELIVERED
                )
        );

        response.setTotalRevenue(
                orderRepository.getTotalRevenue(restaurantId)
        );

        return response;
    }

    private OrderItemResponse convertOrderItemResponse(OrderItem item) {

        OrderItemResponse response = new OrderItemResponse();

        response.setOrderItemId(item.getId());

        response.setMenuItemId(item.getMenuItem().getId());
        response.setMenuItemName(item.getMenuItem().getName());

        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setSubTotal(item.getSubTotal());

        return response;
    }
}
