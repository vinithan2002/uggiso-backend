package com.uggiso.uggiso_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quantity

    @Builder.Default
    @Column(nullable = false)
    private Integer quantity = 1;

    // Price

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Sub Total

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // ================= Cart =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // ================= Menu Item =================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @PrePersist
    public void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        calculateSubTotal();
    }

    @PreUpdate
    public void onUpdate() {

        updatedAt = LocalDateTime.now();

        calculateSubTotal();
    }

    private void calculateSubTotal() {

        if (price != null && quantity != null) {

            subTotal = price.multiply(
                    BigDecimal.valueOf(quantity)
            );

        } else {

            subTotal = BigDecimal.ZERO;

        }

    }
}