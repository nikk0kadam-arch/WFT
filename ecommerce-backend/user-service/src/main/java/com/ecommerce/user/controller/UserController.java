package com.ecommerce.user.controller;

import com.ecommerce.shared.dto.ApiResponse;
import com.ecommerce.shared.dto.AuthRequest;
import com.ecommerce.shared.dto.AuthResponse;
import com.ecommerce.shared.dto.UserDTO;
import com.ecommerce.user.dto.RegisterRequest;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.service.AuthService;
import com.ecommerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User registration, login, and profile endpoints")
@Slf4j
public class UserController {
    
    private final UserService userService;
    private final AuthService authService;
    
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }
    
    @PostMapping("/auth/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody RegisterRequest request) {
        log.info("Registering user: {}", request.getEmail());
        
        User user = userService.registerUser(
                request.getEmail(),
                request.getUsername(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhone()
        );
        
        UserDTO userDTO = userService.getUserDTOById(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(userDTO, "User registered successfully"));
    }
    
    @PostMapping("/auth/login")
    @Operation(summary = "User login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        log.info("User login attempt: {}", request.getEmail());
        
        AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful"));
    }
    
    @PostMapping("/auth/refresh")
    @Operation(summary = "Refresh JWT token")
    public ResponseEntity<ApiResponse<String>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String newToken = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(newToken, "Token refreshed successfully"));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        log.info("Fetching user: {}", id);
        
        UserDTO userDTO = userService.getUserDTOById(id);
        return ResponseEntity.ok(ApiResponse.success(userDTO, "User retrieved successfully"));
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@PathVariable String email) {
        log.info("Fetching user by email: {}", email);
        
        User user = userService.getUserByEmail(email);
        UserDTO userDTO = userService.getUserDTOById(user.getId());
        return ResponseEntity.ok(ApiResponse.success(userDTO, "User retrieved successfully"));
    }
    
    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("UP", "Service is healthy"));
    }
}

@Data
class RefreshTokenRequest {
    public String refreshToken;
}
