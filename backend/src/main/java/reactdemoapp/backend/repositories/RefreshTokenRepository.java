package reactdemoapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import reactdemoapp.backend.models.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Override
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findByUsername(String username);
}
