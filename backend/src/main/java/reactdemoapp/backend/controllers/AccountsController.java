package reactdemoapp.backend.controllers;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactdemoapp.backend.payload.response.JwtResponse;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class AccountsController {
    @Autowired
    JwtEncoder jwtEncoder;

//    @PostMapping("/signUp")
//    public String signUp() {}

    @PostMapping("/signIn")
    public JwtResponse signIn(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 3600L;
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return new JwtResponse(
                authentication.getName(),
                this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                "",
                expiry
        );
    }

//    @PostMapping("/token")
//    public String token() {}

//    @PostMapping("/update")
//    public String update() {}
}
