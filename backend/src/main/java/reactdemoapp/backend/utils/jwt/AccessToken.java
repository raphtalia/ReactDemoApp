package reactdemoapp.backend.utils.jwt;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
public class AccessToken {
    private String email;
    private String token;
    private String type = "Bearer";
    private Instant timeCreated = Instant.now();
    private long expiresIn = 60 * 60; // 1 hour
}
