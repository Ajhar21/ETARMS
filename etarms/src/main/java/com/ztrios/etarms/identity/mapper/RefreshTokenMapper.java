package com.ztrios.etarms.identity.mapper;

import com.ztrios.etarms.identity.entity.RefreshToken;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.identity.dto.TokenResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RefreshTokenMapper {

    public static RefreshToken toEntity(
            User user,
            String tokenValue,
            Instant expiresAt,
            String createdByIp,
            String userAgent
    ) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setUserId(user.getId());
        token.setToken(tokenValue);
        token.setExpiresAt(expiresAt);
        token.setCreatedByIp(createdByIp);
        token.setUserAgent(userAgent);
        return token;
    }

    /**
     * Builds TokenResponse using Lombok builder on record
     */
    public static TokenResponse toTokenResponse(
            String accessToken,
            RefreshToken refreshToken
    ) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiresAt(refreshToken.getExpiresAt())
                .build();
    }
}
