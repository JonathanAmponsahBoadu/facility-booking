package com.university.facility_booking.controller;

import com.university.facility_booking.model.User;
import com.university.facility_booking.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.university.facility_booking.repository.UserRepository;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
private final UserRepository userRepository;
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @DeleteMapping("/{id}")
@PreAuthorize("hasAuthority('ADMIN')")
public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    try {
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully.");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body("Failed to delete user.");
    }
}
}