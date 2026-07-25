package com.uggiso.uggiso_backend.service.impl;

import com.uggiso.uggiso_backend.dto.request.AddToCartRequest;
import com.uggiso.uggiso_backend.dto.request.UpdateCartItemRequest;
import com.uggiso.uggiso_backend.dto.response.CartItemResponse;
import com.uggiso.uggiso_backend.dto.response.CartResponse;
import com.uggiso.uggiso_backend.entity.Cart;
import com.uggiso.uggiso_backend.entity.CartItem;
import com.uggiso.uggiso_backend.entity.MenuItem;
import com.uggiso.uggiso_backend.entity.Users;
import com.uggiso.uggiso_backend.repository.CartItemRepository;
import com.uggiso.uggiso_backend.repository.CartRepository;
import com.uggiso.uggiso_backend.repository.MenuItemRepository;
import com.uggiso.uggiso_backend.repository.UserDetailsRepository;
import com.uggiso.uggiso_backend.service.CartService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserDetailsRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    public CartServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserDetailsRepository userRepository,
            MenuItemRepository menuItemRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public CartResponse addToCart(AddToCartRequest request) {

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setRestaurant(menuItem.getRestaurant());
                    return cartRepository.save(newCart);
                });
        // Allow items from only one restaurant
        if (cart.getRestaurant() != null &&
                !cart.getRestaurant().getId().equals(menuItem.getRestaurant().getId())) {

            throw new RuntimeException(
                    "You can only order from one restaurant at a time."
            );
        }

        CartItem cartItem = cartItemRepository
                .findByCartIdAndMenuItemId(cart.getId(), menuItem.getId())
                .orElse(null);

        if (cartItem == null) {

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(menuItem.getPrice());

            cartItemRepository.save(cartItem);

            cart.getCartItems().add(cartItem);

        } else {

            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity()
            );

            cartItemRepository.save(cartItem);
        }

        cartRepository.save(cart);

        return getCartByUser(user.getId());
    }

    @Override
    public CartResponse getCartByUser(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItemResponse> items = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;
        int totalItems = 0;

        for (CartItem item : cart.getCartItems()) {

            CartItemResponse response = new CartItemResponse();

            response.setCartItemId(item.getId());
            response.setMenuItemId(item.getMenuItem().getId());
            response.setMenuItemName(item.getMenuItem().getName());
            response.setQuantity(item.getQuantity());
            response.setPrice(item.getPrice());
            response.setSubTotal(item.getSubTotal());

            items.add(response);

            total = total.add(item.getSubTotal());
            totalItems += item.getQuantity();
        }

        cart.setTotalAmount(total);
        cart.setTotalItems(totalItems);

        cartRepository.save(cart);

        CartResponse response = new CartResponse();

        response.setCartId(cart.getId());
        response.setUserId(userId);
        response.setRestaurantId(cart.getRestaurant().getId());
        response.setItems(items);
        response.setTotalAmount(total);
        response.setTotalItems(totalItems);

        return response;
    }

    @Override
    public CartResponse updateCartItem(Long cartItemId,
                                       UpdateCartItemRequest request) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(request.getQuantity());

        cartItemRepository.save(cartItem);

        return getCartByUser(cartItem.getCart().getUser().getId());
    }

    @Override
    public void removeCartItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteByCartId(cart.getId());

        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setTotalItems(0);

        cartRepository.save(cart);
    }
}