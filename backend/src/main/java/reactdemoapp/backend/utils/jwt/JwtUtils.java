package reactdemoapp.backend.utils.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.access-token-ttl}")
    int accessTokenTTL; // + 1-minute grace period

    public AccessToken generateAccessToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenTTL))
                .subject(user.getEmail())
                .build();

        return new AccessToken()
                .setEmail(user.getEmail())
                .setToken(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
                .setExpiresIn(accessTokenTTL);
    }
}
