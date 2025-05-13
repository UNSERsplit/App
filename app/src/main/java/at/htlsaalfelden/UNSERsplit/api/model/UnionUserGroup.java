package at.htlsaalfelden.UNSERsplit.api.model;

public class UnionUserGroup {
    public final PublicUserData userData;
    public final Group group;

    public UnionUserGroup(PublicUserData userData, Group group) {
        this.userData = userData;
        this.group = group;
    }


}
