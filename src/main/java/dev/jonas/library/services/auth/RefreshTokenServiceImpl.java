package dev.jonas.library.services.auth;

import dev.jonas.library.entities.RefreshToken;
import dev.jonas.library.entities.User;
import dev.jonas.library.exceptions.UserNotFoundException;
import dev.jonas.library.exceptions.auth.TokenExpiredException;
import dev.jonas.library.repositories.RefreshTokenRepository;
import dev.jonas.library.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    private final Duration refreshTokenDuration = Duration.ofDays(7);

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plus(refreshTokenDuration));
        token.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenExpiredException("Refresh token has expired.");
        }
        return token;
    }

    public long deleteByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return refreshTokenRepository.deleteByUser(user);
    }

    public RefreshToken validateAndGetToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(rt -> rt.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new RuntimeException("Invalid or expired refresh token"));
    }

    public void deleteToken(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    @Transactional
    public void revokeAllTokensForUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public int deleteAllExpiredTokens() {
        List<RefreshToken> expiredTokens = refreshTokenRepository.findAllByExpiryDateBefore(LocalDateTime.now());
        refreshTokenRepository.deleteAll(expiredTokens);
        return expiredTokens.size();
    }
}