package com.uggiso.uggiso_backend.service.impl;
import com.uggiso.uggiso_backend.entity.Role;
import com.uggiso.uggiso_backend.entity.Users;
import com.uggiso.uggiso_backend.repository.UserDetailsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDetailsRepository userDetailsRepository,
                       PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register User
    public Users register(Users user) {

        String userName = user.getUsername();

        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (userDetailsRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userDetailsRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);

        return userDetailsRepository.save(user);
    }

    public Users registerRestaurantOwner(Users user) {

        String userName = user.getUsername();

        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (userDetailsRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userDetailsRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_RESTAURANT_OWNER);

        return userDetailsRepository.save(user);
    }

    public Users registerDeliveryAgent(Users user) {

        String userName = user.getUsername();

        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (userDetailsRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userDetailsRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_DELIVERY_AGENT);

        return userDetailsRepository.save(user);
    }

    // Get Logged-in User
    public Users getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userDetailsRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    // Update Profile
    public Users updateProfile(Users updatedUser) {

        Users user = getCurrentUser();

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhoneNumber(updatedUser.getPhoneNumber());

        return userDetailsRepository.save(user);
    }

     // Change Password
    public void changePassword(String oldPassword,
                               String newPassword) {

        Users user = getCurrentUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userDetailsRepository.save(user);
    }

    // Delete Account
    public void deleteCurrentUser() {

        Users user = getCurrentUser();

        userDetailsRepository.delete(user);
    }

    public Users getUserByEmail(String email) {

        var user = userDetailsRepository.findByEmail(email);

        return user.orElseThrow(() ->
                new RuntimeException("User not found"));
    }
}