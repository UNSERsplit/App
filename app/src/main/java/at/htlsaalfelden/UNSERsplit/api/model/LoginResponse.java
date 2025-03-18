package at.htlsaalfelden.UNSERsplit.api.model;

public class LoginResponse {
    private String access_token;
    private String token_type;
    private int userid;

    public String getToken() {
        return access_token;
    }

    public String getType() {
        return token_type;
    }

    public int getUserid() {
        return userid;
    }
}
