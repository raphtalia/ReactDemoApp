package reactdemoapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import reactdemoapp.backend.models.BlockedJwt;

import java.util.List;

public interface BlockedJwtRepository extends JpaRepository<BlockedJwt, String> {
    @Query(value = "SELECT * FROM public.blocked_jwts WHERE time_created + (INTERVAL '1' SECOND * expires_in) < NOW();", nativeQuery = true)
    List<BlockedJwt> findByExpired();

    BlockedJwt findByToken(String token);
}
