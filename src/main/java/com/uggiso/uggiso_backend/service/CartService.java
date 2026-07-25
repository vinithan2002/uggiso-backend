package com.uggiso.uggiso_backend.service;

import com.uggiso.uggiso_backend.dto.request.AddToCartRequest;
import com.uggiso.uggiso_backend.dto.request.UpdateCartItemRequest;
import com.uggiso.uggiso_backend.dto.response.CartResponse;

public interface CartService {

    CartResponse addToCart(AddToCartRequest request);

    CartResponse getCartByUser(Long userId);

    CartResponse updateCartItem(
            Long cartItemId,
            UpdateCartItemRequest request
    );

    void removeCartItem(Long cartItemId);

    void clearCart(Long userId);
}