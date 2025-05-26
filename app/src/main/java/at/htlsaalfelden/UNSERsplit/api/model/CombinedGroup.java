package at.htlsaalfelden.UNSERsplit.api.model;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.ui.groups.GroupOverviewActivity;

public class CombinedGroup implements CombinedData{
    private Group group;
    private List<PublicUserData> members;
    private double balance;

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

    @Override
    public String getName() {
        return getGroup().getName();
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String getExtra(Context context) {
        return context.getString(R.string.members, getMembers().size());
    }

    @Override
    public Intent getClickIntent(Context ctx) {
        Intent myIntent = new Intent(ctx, GroupOverviewActivity.class);
        myIntent.putExtra("GROUP", getGroup().getGroupid());
        return myIntent;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public int getCombinedId() {
        return -this.group.getGroupid();
    }
}
