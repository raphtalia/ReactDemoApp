package reactdemoapp.backend.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import reactdemoapp.backend.models.AccessToken;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    @Autowired
    JwtEncoder jwtEncoder;

    private int jwtLifetime = 3600; // 1 hour

    public AccessToken generateAccessToken(Authentication auth) {
        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtLifetime))
                .subject(auth.getName())
                .claim("scope", scope)
                .build();

        return new AccessToken(
                auth.getName(),
                this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                jwtLifetime
        );
    }

    public AccessToken generateAccessTokenFromUsername(String username) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtLifetime))
                .subject(username)
                .build();

        return new AccessToken(
                username,
                this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                jwtLifetime
        );
    }
}
