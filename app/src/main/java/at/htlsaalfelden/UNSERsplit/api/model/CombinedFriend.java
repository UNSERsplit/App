package at.htlsaalfelden.UNSERsplit.api.model;

public class CombinedFriend {
    private PublicUserData userData;
    private FriendData friendData;

    public CombinedFriend(PublicUserData userData, FriendData friendData) {
        this.userData = userData;
        this.friendData = friendData;
    }

    public PublicUserData getUserData() {
        return userData;
    }

    public FriendData getFriendData() {
        return friendData;
    }
}
