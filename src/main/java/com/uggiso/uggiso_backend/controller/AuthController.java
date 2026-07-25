package com.uggiso.uggiso_backend.controller;

import com.uggiso.uggiso_backend.dto.response.LoginResponse;
import com.uggiso.uggiso_backend.entity.Restaurant;
import com.uggiso.uggiso_backend.entity.Role;
import com.uggiso.uggiso_backend.entity.Users;
import com.uggiso.uggiso_backend.repository.RestaurantRepository;
import com.uggiso.uggiso_backend.service.impl.UserService;
import com.uggiso.uggiso_backend.util.JWTUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.uggiso.uggiso_backend.dto.request.LoginRequest;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final RestaurantRepository restaurantRepository;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JWTUtil jwtUtil, RestaurantRepository restaurantRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody Users user) {
        try {
            userService.register(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/restaurant-owner/register")
    public ResponseEntity<Users> registerRestaurantOwner(
            @RequestBody Users user) {

        return ResponseEntity.ok(
                userService.registerRestaurantOwner(user)
        );
    }

    @PostMapping("/delivery-agent/register")
    public ResponseEntity<Users> registerDeliveryAgent(
            @RequestBody Users user) {

        return ResponseEntity.ok(
                userService.registerDeliveryAgent(user)
        );
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest) {

            try {
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getEmail(),
                                    loginRequest.getPassword()
                            )
                    );

            Users user = userService.getUserByEmail(
                    loginRequest.getEmail()
            );
                Long restaurantId = null;

                if (user.getRole() == Role.ROLE_RESTAURANT_OWNER) {

                    Restaurant restaurant = restaurantRepository
                            .findByOwnerId(user.getId())
                            .orElse(null);

                    if (restaurant != null) {
                        restaurantId = restaurant.getId();
                    }
                }

            String token = jwtUtil.generateToken(user.getEmail());

            LoginResponse response = new LoginResponse(

                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    restaurantId,
                    token

            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {

            return ResponseEntity.badRequest()
                    .body("Invalid Username or Password");
        }

    }
}
