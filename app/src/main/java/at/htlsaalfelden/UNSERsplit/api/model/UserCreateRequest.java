package at.htlsaalfelden.UNSERsplit.api.model;

public class UserCreateRequest {
    private String firstname;
    private String lastname;
    private String iban;
    private String email;
    private String password;

    public UserCreateRequest(String firstname, String lastname, String email, String iban, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.iban = iban;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getIban() {
        return iban;
    }

    public String getPassword() {
        return password;
    }
}
