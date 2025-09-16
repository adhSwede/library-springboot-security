package dev.jonas.library.repositories;

import dev.jonas.library.entities.RefreshToken;
import dev.jonas.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);

    List<RefreshToken> findAllByUser(User user);

    List<RefreshToken> findAllByExpiryDateBefore(LocalDateTime date);
    
    boolean existsByToken(String token);

}

