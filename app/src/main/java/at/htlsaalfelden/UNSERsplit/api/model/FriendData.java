package at.htlsaalfelden.UNSERsplit.api.model;

public class FriendData {
    private int id;
    private int inviting_userid;
    private int invited_userid;
    private boolean pending;


    public int getId() {
        return id;
    }

    public int getInviting_userid() {
        return inviting_userid;
    }

    public int getInvited_userid() {
        return invited_userid;
    }

    public boolean isPending() {
        return pending;
    }
}
