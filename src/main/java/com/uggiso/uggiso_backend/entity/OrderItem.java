package com.uggiso.uggiso_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quantity ordered
    @Column(nullable = false)
    private Integer quantity;

    // Price at the time of order
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // quantity × price
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ================= Order =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    // ================= Menu Item =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @PrePersist
    public void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (price != null && quantity != null) {
            subTotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }

    @PreUpdate
    public void onUpdate() {

        updatedAt = LocalDateTime.now();

        if (price != null && quantity != null) {
            subTotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }
}