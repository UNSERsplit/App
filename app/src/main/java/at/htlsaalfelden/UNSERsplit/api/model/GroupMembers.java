package at.htlsaalfelden.UNSERsplit.api.model;

public class GroupMembers {
    private final int memberId;
    private final int userId;
    private final int groupId;
    private final boolean pending;

    public GroupMembers(int memberId, int userId, boolean pending, int groupId) {
        this.memberId = memberId;
        this.userId = userId;
        this.pending = pending;
        this.groupId = groupId;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getUserId() {
        return userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public boolean isPending() {
        return pending;
    }
}
