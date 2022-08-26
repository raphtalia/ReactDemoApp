package reactdemoapp.backend.models;

import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "refresh_token")
@RequiredArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Instant expirationDate;

    public RefreshToken(String token, String username, Instant expirationDate) {
        this.token = token;
        this.username = username;
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }
}
