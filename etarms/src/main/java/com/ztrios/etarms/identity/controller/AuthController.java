package com.ztrios.etarms.identity.controller;

import com.ztrios.etarms.common.response.ApiResponse;
import com.ztrios.etarms.identity.dto.*;
import com.ztrios.etarms.identity.entity.RefreshToken;
import com.ztrios.etarms.identity.service.AuthService;
import com.ztrios.etarms.identity.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    // ==================== LOGIN ============================
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse response = authService.login(request, httpRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Login successful",
                        response
                )
        );
    }

    // ==================== REFRESH TOKEN ============================
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {

        TokenResponse response = authService.refreshAccessToken(request);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Refresh token generated successfully",
                        response
                )
        );
    }

    // ==================== LOGOUT ============================
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(@Valid @RequestBody LogoutRequest request) {

        // Fetch the refresh token entity by token string
        RefreshToken refreshToken = refreshTokenService.getByToken(request.refreshToken());

        // Revoke using existing method
        refreshTokenService.revokeToken(refreshToken, "user logout");

        LogoutResponse response = new LogoutResponse("Logout successful", 1);
        // Directly return record without creating a local variable
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Logout successful",
                        response
                )
        );
    }

    // ==================== LOGOUT ALL ============================
    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<LogoutResponse>> logoutAll(@Valid @RequestParam String username) {

        // Revoke all refresh tokens for this user, returns number of revoked tokens
        int revokedCount = refreshTokenService.revokeAllTokensForUser(username, "user logout from all devices");

        LogoutResponse response = new LogoutResponse("All devices logout successfully", revokedCount);

        // Directly return record
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "All devices logout successfully",
                        response
                )
        );
    }

}
