package reactdemoapp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactdemoapp.backend.models.RefreshToken;
import reactdemoapp.backend.repositories.RefreshTokenRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private Long refreshTokenDuration = 3600L;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);

        if (refreshToken.isPresent()) {
            if (refreshToken.get().getExpirationDate().isAfter(Instant.now())) {
                return refreshToken;
            } else {
                refreshTokenRepository.delete(refreshToken.get());
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public List<RefreshToken> findByAuth(Authentication auth) {
        return refreshTokenRepository.findByUsername(auth.getName());
    }

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                username,
                Instant.now().plusSeconds(refreshTokenDuration)
        );
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public void invalidateRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    public void invalidateRefreshTokens(Authentication auth) {
        refreshTokenRepository.deleteAll(findByAuth(auth));
    }
}
