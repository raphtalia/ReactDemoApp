package reactdemoapp.backend.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
public class JwtResponse {
    private String email;
    private String type = "Bearer";
    private String access_token;
    private Instant access_token_time_created = Instant.now();
    private long access_token_expires_in = 60 * 60; // 1 hour
    private String refresh_token;
    private Instant refresh_token_time_created = Instant.now();
    private long refresh_token_expires_in = 60 * 60 * 24 * 28; // 28 days
}
