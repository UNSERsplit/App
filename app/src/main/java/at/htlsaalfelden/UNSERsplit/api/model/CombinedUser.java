package at.htlsaalfelden.UNSERsplit.api.model;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

public class CombinedUser implements CombinedData{
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

    @Override
    public String getName() {
        return getUserData().getFirstname() + " " + getUserData().getLastname();
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String getExtra() {
        return "Person";
    }

    @Override
    public Intent getClickIntent(Context ctx) {
        return null; //TODO create user overview
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
