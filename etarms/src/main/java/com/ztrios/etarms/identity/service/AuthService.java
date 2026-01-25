package com.ztrios.etarms.identity.service;

import com.ztrios.etarms.common.exception.ResourceNotFoundException;
import com.ztrios.etarms.identity.dto.AuthRequest;
import com.ztrios.etarms.identity.dto.AuthResponse;
import com.ztrios.etarms.identity.dto.RefreshTokenRequest;
import com.ztrios.etarms.identity.dto.TokenResponse;
import com.ztrios.etarms.identity.entity.RefreshToken;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.identity.repository.UserRepository;
import com.ztrios.etarms.identity.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    /**
     * Refresh access token
     */
    public TokenResponse refreshAccessToken(RefreshTokenRequest request) {

        // Step 1: fetch the RefreshToken entity from DB
        RefreshToken refreshToken = refreshTokenService
                .getByToken(request.getRefreshToken());

        refreshToken = refreshTokenService.verifyExpiration(refreshToken); // returns RefreshToken

        User user = refreshToken.getUser();

        // Rotate refresh token: revoke old, issue new
        refreshTokenService.revokeToken(refreshToken, "rotated");
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(
                user, refreshToken.getCreatedByIp(), refreshToken.getUserAgent()
        );

        String newAccessToken = jwtProvider.generateToken(user.getUsername(), user.getRole().getName());

        return new TokenResponse(newAccessToken, newRefreshToken.getToken(), newRefreshToken.getExpiresAt());
    }

    @Transactional
    public AuthResponse login(AuthRequest request, HttpServletRequest httpRequest) {

        // Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Load Spring Security user
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());

        // Load domain User entity
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //  Generate access token
        String accessToken = jwtProvider.generateToken(
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()
        );

        // Extract client metadata
        String clientIp = resolveClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        // Create refresh token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user, clientIp, userAgent);

        // Return response
        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken.getToken())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }

    /**
     * Handles proxy / load balancer scenarios safely
     */
    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
