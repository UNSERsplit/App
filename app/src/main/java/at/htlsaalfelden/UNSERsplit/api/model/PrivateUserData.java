package at.htlsaalfelden.UNSERsplit.api.model;

public class PrivateUserData {
    private String email;
    private String password;

    public PrivateUserData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
