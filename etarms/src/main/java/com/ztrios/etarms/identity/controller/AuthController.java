package com.ztrios.etarms.identity.controller;

import com.ztrios.etarms.identity.dto.*;
import com.ztrios.etarms.identity.entity.RefreshToken;
import com.ztrios.etarms.identity.service.AuthService;
import com.ztrios.etarms.identity.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    /*
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request, HttpServletRequest httpRequest) {

        try {
            // Authenticate credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

//            // Generate JWT
//            String token = jwtProvider.generateToken(
//                    userDetails.getUsername(),
//                    userDetails.getAuthorities().iterator().next().getAuthority()
//            );
//
//            // Return response
//            return ResponseEntity.ok(
//                    new AuthResponse(token, userDetails.getUsername(),
//                            userDetails.getAuthorities().iterator().next().getAuthority())
//            );

            // Load User entity from DB (needed to create refresh token)
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate access token
            String accessToken = jwtProvider.generateToken(
                    userDetails.getUsername(),
                    userDetails.getAuthorities().iterator().next().getAuthority()
            );

            // Extract IP and User-Agent for auditing
            String clientIp = httpRequest.getRemoteAddr();
            String userAgent = httpRequest.getHeader("User-Agent");

            // Create refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, clientIp, userAgent);

            // Return response with record
            AuthResponse response = new AuthResponse(
                    accessToken,
                    refreshToken.getToken(),
                    user.getUsername(),
                    user.getRole().getName()
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).build();
        }
    }
*/

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse response = authService.login(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse response = authService.refreshAccessToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody LogoutRequest request) {

        // Fetch the refresh token entity by token string
        RefreshToken refreshToken = refreshTokenService.getByToken(request.refreshToken());

        // Revoke using existing method
        refreshTokenService.revokeToken(refreshToken, "user logout");

        // Directly return record without creating a local variable
        return ResponseEntity.ok(new LogoutResponse("Logout successful", 1));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<LogoutResponse> logoutAll(@Valid @RequestParam String username) {

        // Revoke all refresh tokens for this user, returns number of revoked tokens
        int revokedCount = refreshTokenService.revokeAllTokensForUser(username, "user logout from all devices");

        // Directly return record
        return ResponseEntity.ok(new LogoutResponse("All devices logged out successfully", revokedCount));
    }

}
