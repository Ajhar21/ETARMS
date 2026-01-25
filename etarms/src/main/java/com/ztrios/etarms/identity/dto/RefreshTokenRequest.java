package com.ztrios.etarms.identity.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Immutable request DTO for refresh token API.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true) // required by Jackson for deserialization
public class RefreshTokenRequest {
    private final String refreshToken;
}
