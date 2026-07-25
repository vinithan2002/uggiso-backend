package com.uggiso.uggiso_backend.controller;

import com.uggiso.uggiso_backend.dto.request.AddToCartRequest;
import com.uggiso.uggiso_backend.dto.request.UpdateCartItemRequest;
import com.uggiso.uggiso_backend.dto.response.CartResponse;
import com.uggiso.uggiso_backend.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ================= Add Item To Cart =================

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @Valid @RequestBody AddToCartRequest request) {

        return new ResponseEntity<>(
                cartService.addToCart(request),
                HttpStatus.CREATED
        );
    }

    // ================= Get User Cart =================

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.getCartByUser(userId)
        );
    }

    // ================= Update Quantity =================

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        return ResponseEntity.ok(
                cartService.updateCartItem(cartItemId, request)
        );
    }

    // ================= Remove Item =================

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<String> removeCartItem(
            @PathVariable Long cartItemId) {

        cartService.removeCartItem(cartItemId);

        return ResponseEntity.ok("Cart item removed successfully.");
    }

    // ================= Clear Cart =================

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(
            @PathVariable Long userId) {

        cartService.clearCart(userId);

        return ResponseEntity.ok("Cart cleared successfully.");
    }
}