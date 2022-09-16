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
@Table(schema = "public", name = "refresh_tokens")
public class RefreshToken {
    @Id
    @Column(name = "token")
    private String token;
    private String jwt;
    private long userId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCreated = new Date();
    private int expiresIn = 60 * 60 * 24 * 28; // 28 days
}
