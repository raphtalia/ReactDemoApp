package reactdemoapp.backend.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import reactdemoapp.backend.payload.response.UserResponse;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity
@Table(schema = "public", name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;
    private boolean enabled = true;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private LocalDate dob;
    private Timestamp timeCreated = new Timestamp(System.currentTimeMillis());

    public UserResponse toResponse() {
        return new UserResponse()
                .setUserId(userId)
                .setEnabled(enabled)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setDob(dob)
                .setTimeCreated(timeCreated);
    }
}
