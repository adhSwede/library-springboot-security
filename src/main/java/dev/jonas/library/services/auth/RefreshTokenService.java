package dev.jonas.library.services.auth;

import dev.jonas.library.entities.RefreshToken;
import dev.jonas.library.entities.User;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);

    long deleteByUserId(Long userId);

    RefreshToken validateAndGetToken(String token);

    void deleteToken(RefreshToken token);

    void revokeAllTokensForUser(User user);

    int deleteAllExpiredTokens();
}
