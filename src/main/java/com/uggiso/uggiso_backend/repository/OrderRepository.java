package com.uggiso.uggiso_backend.repository;

import com.uggiso.uggiso_backend.entity.OrderStatus;
import com.uggiso.uggiso_backend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByUserId(Long userId);

    List<Orders> findByRestaurantId(Long restaurantId);

    List<Orders> findByOrderStatus(OrderStatus orderStatus);

    Orders findByOrderNumber(String orderNumber);

    long countByRestaurantId(Long restaurantId);

    long countByRestaurantIdAndOrderStatus(
            Long restaurantId,
            OrderStatus orderStatus
    );

    @Query("""
SELECT COALESCE(SUM(o.finalAmount),0)
FROM Orders o
WHERE o.restaurant.id = :restaurantId
AND o.orderStatus = 'DELIVERED'
""")
    BigDecimal getTotalRevenue(@Param("restaurantId") Long restaurantId);

    List<Orders> findByDeliveryAgentIsNullAndOrderStatus(
            OrderStatus orderStatus
    );
}