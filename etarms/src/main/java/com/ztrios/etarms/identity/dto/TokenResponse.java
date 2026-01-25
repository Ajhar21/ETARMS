package com.ztrios.etarms.identity.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken,
        Instant refreshTokenExpiresAt
) {
}
