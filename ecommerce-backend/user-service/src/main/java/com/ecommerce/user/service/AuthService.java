package com.ecommerce.user.service;

import com.ecommerce.shared.dto.AuthResponse;
import com.ecommerce.shared.dto.UserDTO;
import com.ecommerce.shared.exception.UnauthorizedException;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {
    
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthService(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    public AuthResponse login(String email, String password) {
        User user = userService.getUserByEmail(email);
        
        if (!userService.validatePassword(password, user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new UnauthorizedException("User account is not active");
        }
        
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        
        return new AuthResponse(
                token,
                refreshToken,
                userDTO,
                "Bearer",
                jwtTokenProvider.getJwtExpirationMs() / 1000
        );
    }
    
    public String refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }
        
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userId);
        
        return jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());
    }
}
