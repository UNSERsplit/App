package at.htlsaalfelden.UNSERsplit.api.model;

public class User extends UserCreateRequest {
    private int userid;

    public User(String firstname, String lastname, String email, String iban, String password) {
        super(firstname, lastname, email, iban, password);
    }

    public int getUserid() {
        return userid;
    }
}
