package at.htlsaalfelden.UNSERsplit.api.model;

import android.widget.BaseAdapter;

public class CombinedUser {
    private PublicUserData userData;
    private BaseAdapter adapter;
    private double balance;

    public CombinedUser(PublicUserData userData, double balance) {
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;

        this.adapter.notifyDataSetChanged();
    }

    public void setBalanceNoNotify(double balance) {
        this.balance = balance;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }
}
