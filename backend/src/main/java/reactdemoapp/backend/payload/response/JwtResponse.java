package reactdemoapp.backend.payload.response;

public class JwtResponse {
    private String username;
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private long expiresIn;

    public JwtResponse(String username, String token, String refreshToken, long expiresIn) {
        this.username = username;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
