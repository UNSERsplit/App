package at.htlsaalfelden.UNSERsplit.api.model;

public class GroupCreateRequest {
    private String name;

    public GroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
