package reactdemoapp.backend.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
public class UserResponse {
    private long userId;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private Timestamp timeCreated;
}
