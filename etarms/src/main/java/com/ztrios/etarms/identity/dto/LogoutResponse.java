package com.ztrios.etarms.identity.dto;

/**
 * Can be used for single-device logout or all-device logout.
 */
public record LogoutResponse(
        String message,       // Optional confirmation message
        int revokedCount      // Number of refresh tokens revoked (always 1 for single-device logout)
) {
}
