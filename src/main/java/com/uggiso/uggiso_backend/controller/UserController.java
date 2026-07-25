package com.uggiso.uggiso_backend.controller;

import com.uggiso.uggiso_backend.entity.Users;
import com.uggiso.uggiso_backend.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get current user profile
    @GetMapping("/profile")
    public ResponseEntity<Users> getProfile() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    // Update profile
    @PutMapping("/profile")
    public ResponseEntity<Users> updateProfile(
            @Valid @RequestBody Users user) {

        return ResponseEntity.ok(userService.updateProfile(user));
    }

    // Change Password
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {

        userService.changePassword(oldPassword, newPassword);

        return ResponseEntity.ok("Password changed successfully");
    }

    // Delete Account
    @DeleteMapping
    public ResponseEntity<String> deleteAccount() {

        userService.deleteCurrentUser();

        return ResponseEntity.ok("Account deleted successfully");
    }
}