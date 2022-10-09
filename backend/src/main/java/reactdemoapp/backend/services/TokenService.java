package reactdemoapp.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import reactdemoapp.backend.models.BlockedJwt;
import reactdemoapp.backend.models.RefreshToken;
import reactdemoapp.backend.models.User;
import reactdemoapp.backend.repositories.BlockedJwtRepository;
import reactdemoapp.backend.repositories.RefreshTokenRepository;
import reactdemoapp.backend.repositories.UserRepository;
import reactdemoapp.backend.utils.jwt.AccessToken;
import reactdemoapp.backend.utils.jwt.JwtUtils;

import java.sql.Timestamp;

@Service
public class TokenService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    BlockedJwtRepository blockedJwtRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JwtDecoder jwtDecoder;

    @Value("${jwt.refresh-token-ttl}")
    int refreshTokenTTL;

    private void deleteExpiredTokens() {
        refreshTokenRepository.findByExpired().forEach(token -> refreshTokenRepository.delete(token));
        blockedJwtRepository.findByExpired().forEach(token -> blockedJwtRepository.delete(token));
    }

    public RefreshToken generateRefreshToken(User user, String jwt) {
        deleteExpiredTokens();
        return refreshTokenRepository.save(new RefreshToken()
                .setToken(java.util.UUID.randomUUID().toString())
                .setJwt(jwt)
                .setUserId(user.getUserId())
                .setExpiresIn(refreshTokenTTL));
    }

    public AccessToken useRefreshToken(String token) {
        deleteExpiredTokens();
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);

        if (refreshToken == null) {
            return null;
        }

        // Block the JWT associated with the refresh token
        Jwt jwt = jwtDecoder.decode(refreshToken.getJwt());
        Timestamp issuedAt = new Timestamp(jwt.getIssuedAt().toEpochMilli());
        int expiresIn = (int) (jwt.getExpiresAt().toEpochMilli() - jwt.getIssuedAt().toEpochMilli()) / 1000;
        blockedJwtRepository.save(new BlockedJwt()
                .setToken(refreshToken.getJwt())
                .setUserId(refreshToken.getUserId())
                .setTimeCreated(issuedAt)
                .setExpiresIn(expiresIn));

        refreshTokenRepository.delete(refreshToken);
        return jwtUtils.generateAccessToken(userRepository.findByUserId(refreshToken.getUserId()));
    }
}
