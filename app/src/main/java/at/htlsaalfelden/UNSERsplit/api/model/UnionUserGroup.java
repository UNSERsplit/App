package at.htlsaalfelden.UNSERsplit.api.model;

import androidx.annotation.NonNull;

public class UnionUserGroup {
    public final PublicUserData userData;
    public final Group group;

    public UnionUserGroup(PublicUserData userData, Group group) {
        this.userData = userData;
        this.group = group;
    }

    @NonNull
    @Override
    public String toString() {
        if(this.userData != null) {
            return this.userData.toString();
        } else if(this.group != null) {
            return this.group.toString();
        }
        return "???";
    }
}
