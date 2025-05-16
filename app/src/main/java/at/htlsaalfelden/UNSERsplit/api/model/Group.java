package at.htlsaalfelden.UNSERsplit.api.model;

public class Group {
    private int groupid;
    private String name;
    private int adminuser_userid;

    public int getGroupid() {
        return groupid;
    }

    public String getName() {
        return name;
    }

    public int getAdminuser_userid() {
        return adminuser_userid;
    }

    public Group(String name) {
        this.name = name;
        this.groupid = -1;
        this.adminuser_userid = -1;
    }
}
