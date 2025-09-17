package dev.jonas.library.services.auth;

import dev.jonas.library.entities.User;
import dev.jonas.library.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Service to handle login attempts and account locking.
 */
@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {
    private static final int maxFailedAttempts = 5;
    private static final Duration lockoutDuration = Duration.ofMinutes(15);
    
    private final UserRepository userRepository;

    @Override
    public boolean isLocked(User user) {
        return user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now());
    }

    @Override
    public void recordFailedAttempt(User user) {
        long newCount = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(newCount);

        if (newCount >= maxFailedAttempts) {
            user.setLockedUntil(LocalDateTime.now().plus(lockoutDuration));
        }

        userRepository.save(user);
    }

    @Override
    public void reset(User user) {
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        userRepository.save(user);
    }
}

