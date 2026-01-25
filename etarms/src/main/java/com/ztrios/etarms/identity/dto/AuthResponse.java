package com.ztrios.etarms.identity.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String refreshToken,
        String username,
        String role
) {
}
