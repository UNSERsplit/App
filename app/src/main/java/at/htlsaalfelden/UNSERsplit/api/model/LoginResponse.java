package at.htlsaalfelden.UNSERsplit.api.model;

public class LoginResponse {
    private String token;
    private String expiration;

    public String getToken() {
        return token;
    }

    public String getExpiration() {
        return expiration;
    }
}
