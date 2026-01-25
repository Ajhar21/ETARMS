package com.ztrios.etarms.identity.repository;

import com.ztrios.etarms.identity.entity.RefreshToken;
import com.ztrios.etarms.identity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenAndRevokedTrue(String token);

    List<RefreshToken> findAllByUserAndRevokedFalse(User user);

    void deleteAllByUser(User user);
}
