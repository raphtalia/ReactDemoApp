package reactdemoapp.backend.models;

public class AccessToken {
    private String username;
    private String token;
    private String type = "Bearer";
    private long expiresIn;

    public AccessToken(String username, String token, long expiresIn) {
        this.username = username;
        this.token = token;
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

    public long getExpiresIn() {
        return expiresIn;
    }
}
