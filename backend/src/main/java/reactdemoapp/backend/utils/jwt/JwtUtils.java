package reactdemoapp.backend.utils.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import reactdemoapp.backend.models.User;

import java.time.Instant;

@Component
public class JwtUtils {
    @Autowired
    JwtEncoder jwtEncoder;

    // TODO: Move to application.properties
    private int jwtLifetime = 60 * 60; // 1 hour + 1-minute grace period

    public AccessToken generateAccessToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtLifetime))
                .subject(user.getEmail())
                .build();

        return new AccessToken()
                .setEmail(user.getEmail())
                .setToken(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
                .setExpiresIn(jwtLifetime);
    }
}
