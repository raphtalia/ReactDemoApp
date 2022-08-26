package reactdemoapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import reactdemoapp.backend.models.RefreshToken;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    @Query(value = "SELECT * FROM public.refresh_tokens WHERE time_created + (INTERVAL '1' SECOND * expires_in) < NOW();", nativeQuery = true)
    List<RefreshToken> findByExpired();

    List<RefreshToken> findByUserId(long userId);

    RefreshToken findByToken(String token);
}