package dev.jonas.library.services.auth;

import dev.jonas.library.entities.User;

public interface LoginAttemptService {
    boolean isLocked(User user);

    void recordFailedAttempt(User user);

    void reset(User user);
}
