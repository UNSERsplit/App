package at.htlsaalfelden.UNSERsplit.api.model;

import android.widget.BaseAdapter;

public class CombinedUser {
    private PublicUserData userData;
    private BaseAdapter adapter;
    private int balance;

    public CombinedUser(PublicUserData userData, int balance) {
        this.userData = userData;
        this.balance = balance;
    }

    public PublicUserData getUserData() {
        return userData;
    }

    public void setUserData(PublicUserData userData) {
        this.userData = userData;

        this.adapter.notifyDataSetChanged();
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;

        this.adapter.notifyDataSetChanged();
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }
}
