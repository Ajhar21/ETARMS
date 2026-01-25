package com.ztrios.etarms.identity.service;

import com.ztrios.etarms.common.exception.RefreshTokenExpiredException;
import com.ztrios.etarms.common.exception.RefreshTokenNotFoundException;
import com.ztrios.etarms.common.exception.ResourceNotFoundException;
import com.ztrios.etarms.identity.entity.RefreshToken;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.identity.mapper.RefreshTokenMapper;
import com.ztrios.etarms.identity.repository.RefreshTokenRepository;
import com.ztrios.etarms.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // Configurable lifetime
    private final long refreshTokenDurationDays = 10; // 10 days

    /**
     * Create new refresh token for a user
     */
    public RefreshToken createRefreshToken(User user, String ip, String userAgent) {
//        RefreshToken token = new RefreshToken();
//        token.setUser(user);
//        token.setUserId(user.getId());
//        token.setToken(UUID.randomUUID().toString());
//        token.setExpiresAt(Instant.now().plus(refreshTokenDurationDays, ChronoUnit.DAYS));
//        token.setCreatedByIp(ip);
//        token.setUserAgent(userAgent);

        RefreshToken token = RefreshTokenMapper.toEntity(
                user, UUID.randomUUID().toString(), Instant.now().plus(refreshTokenDurationDays, ChronoUnit.DAYS), ip, userAgent
        );
        return refreshTokenRepository.save(token);
    }

    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(RefreshTokenNotFoundException::new);
    }

    /**
     * Validate token and return it
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().isBefore(Instant.now()) || token.isRevoked()) {
            revokeToken(token, "expired");
            throw new RefreshTokenExpiredException("Refresh token is expired or revoked");
        }
        return token;
    }

    /**
     * Revoke token
     */
    public void revokeToken(RefreshToken token, String reason) {
        if (refreshTokenRepository.findByTokenAndRevokedTrue(token.getToken()).isEmpty()) {
            token.setRevoked(true);
            token.setRevokedReason(reason);
            token.setRevokedAt(Instant.now());
            refreshTokenRepository.save(token);
        } else {
            throw new RefreshTokenExpiredException("Refresh token is expired or revoked");
        }
    }

    /**
     * Revoke all tokens for a user
     */
//    public void revokeAllTokensForUser(User user, String reason) {
//        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);
//        tokens.forEach(token -> {
//            token.setRevoked(true);
//            token.setRevokedReason(reason);
//            token.setRevokedAt(Instant.now());
//        });
//        refreshTokenRepository.saveAll(tokens);
//    }
    public int revokeAllTokensForUser(String userName, String reason) {
        // Fetch the user
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);

        // Revoke each token
        tokens.forEach(token -> {
            token.setRevoked(true);
            token.setRevokedReason(reason);
            token.setRevokedAt(Instant.now());
        });

        // Save all changes
        refreshTokenRepository.saveAll(tokens);

        // Return the number of revoked tokens
        return tokens.size();
    }

}
