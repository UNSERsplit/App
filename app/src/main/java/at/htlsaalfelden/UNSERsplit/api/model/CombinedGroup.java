package at.htlsaalfelden.UNSERsplit.api.model;

import java.util.List;

public class CombinedGroup {
    private Group group;
    private List<PublicUserData> members;
    private int balance;

    public CombinedGroup(Group group, List<PublicUserData> members, int balance) {
        this.group = group;
        this.members = members;
        this.balance = balance;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<PublicUserData> getMembers() {
        return members;
    }

    public void setMembers(List<PublicUserData> members) {
        this.members = members;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
