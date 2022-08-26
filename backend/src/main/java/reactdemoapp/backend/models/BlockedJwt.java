package reactdemoapp.backend.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity
@Table(schema = "public", name = "blocked_jwts")
public class BlockedJwt {
    @Id
    @Column(name = "token")
    private String token;
    private long userId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCreated = new Date();
    private int expiresIn;
}
