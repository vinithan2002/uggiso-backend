package com.uggiso.uggiso_backend.config;

import com.uggiso.uggiso_backend.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers(
                                "/register",
                                "/login",
                                "/restaurant-owner/register",
                                "/delivery-agent/register",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/restaurants/my")
                                .hasRole("RESTAURANT_OWNER")

                                .requestMatchers(HttpMethod.PATCH, "/api/restaurants/**")
                                .hasRole("RESTAURANT_OWNER")

                        // Everyone logged in can VIEW restaurants
                        .requestMatchers(HttpMethod.GET, "/api/restaurants/**")
                        .hasAnyRole("USER", "RESTAURANT_OWNER", "DELIVERY_AGENT")

                        // Everyone logged in can VIEW menu
                        .requestMatchers(HttpMethod.GET, "/api/menu-items/**")
                        .hasAnyRole("USER", "RESTAURANT_OWNER", "DELIVERY_AGENT")

                        // Everyone logged in can VIEW categories
                        .requestMatchers(HttpMethod.GET, "/api/categories/**")
                        .hasAnyRole("USER", "RESTAURANT_OWNER", "DELIVERY_AGENT")

                        // Restaurant Owner CRUD
                        .requestMatchers(HttpMethod.POST, "/api/restaurants/**")
                        .hasRole("RESTAURANT_OWNER")

                        .requestMatchers(HttpMethod.PUT, "/api/restaurants/**")
                        .hasRole("RESTAURANT_OWNER")

                        .requestMatchers(HttpMethod.DELETE, "/api/restaurants/**")
                        .hasRole("RESTAURANT_OWNER")

                        // Menu CRUD
                        .requestMatchers(HttpMethod.POST, "/api/menu-items/**")
                        .hasRole("RESTAURANT_OWNER")

                        .requestMatchers(HttpMethod.PUT, "/api/menu-items/**")
                        .hasRole("RESTAURANT_OWNER")

                        .requestMatchers(HttpMethod.DELETE, "/api/menu-items/**")
                        .hasRole("RESTAURANT_OWNER")

                                // ================= CUSTOMER =================

// Cart
                                .requestMatchers("/api/cart/**")
                                .hasRole("USER")

// Address
                                .requestMatchers("/api/addresses/**")
                                .hasRole("USER")

// Reviews
                                .requestMatchers("/api/reviews/**")
                                .hasRole("USER")

// Payments
                                .requestMatchers("/api/payments/**")
                                .hasRole("USER")

// Customer Orders
                                .requestMatchers(HttpMethod.POST, "/api/orders")
                                .hasRole("USER")

                                .requestMatchers(HttpMethod.GET, "/api/orders/user/**")
                                .hasRole("USER")

                                .requestMatchers(HttpMethod.DELETE, "/api/orders/**")
                                .hasRole("USER")

                                // ================= RESTAURANT OWNER =================

                                // Owner Order APIs
                                .requestMatchers(HttpMethod.GET, "/api/orders/restaurant/**")
                                .hasRole("RESTAURANT_OWNER")

                                .requestMatchers(HttpMethod.PUT, "/api/orders/*/status")
                                .hasRole("RESTAURANT_OWNER")

                        // Delivery
                        .requestMatchers("/api/delivery/**")
                        .hasRole("DELIVERY_AGENT")

                        .anyRequest().authenticated()
                );
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
