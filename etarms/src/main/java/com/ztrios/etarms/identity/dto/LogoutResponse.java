package com.ztrios.etarms.identity.dto;

/**
 * Response returned after a logout action.
 * Can be used for single-device logout or all-device logout.
 */
public record LogoutResponse(
        String message,       // Optional confirmation message
        int revokedCount      // Number of refresh tokens revoked (use 1 for single-device logout)
) {
}
