package at.htlsaalfelden.UNSERsplit.api.model;

public class PublicUserData {
    private String firstname;
    private String lastname;
    private String iban;

    private int userid;

    public PublicUserData(String firstname, String lastname, String iban) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.iban = iban;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getIban() {
        return iban;
    }

    public int getUserid() {
        return userid;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
