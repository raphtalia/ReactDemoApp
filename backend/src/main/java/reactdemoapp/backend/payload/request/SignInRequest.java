package reactdemoapp.backend.payload.request;

import lombok.Getter;

@Getter
public class SignInRequest {
    private String email;
    private String password;
}
