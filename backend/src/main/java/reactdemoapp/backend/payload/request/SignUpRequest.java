package reactdemoapp.backend.payload.request;

import lombok.Getter;
import reactdemoapp.backend.models.User;

import java.time.LocalDate;

@Getter
public class SignUpRequest {
    private String first_name;
    private String last_name;
    private String password;
    private String email;
    private LocalDate dob;

    public User toModel() {
        return new User()
                .setFirstName(first_name)
                .setLastName(last_name)
                .setPassword(password)
                .setEmail(email)
                .setDob(dob);
    }
}
