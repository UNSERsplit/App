package at.htlsaalfelden.UNSERsplit.api.model;

import androidx.annotation.NonNull;

public class Transaction {
    private int touserid;
    private double amount;
    private Integer groupid;
    private int transactionid;
    private int fromuserid;
    private String date;

    public int getTouserid() {
        return touserid;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public int getTransactionid() {
        return transactionid;
    }

    public int getFromuserid() {
        return fromuserid;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "touserid=" + touserid +
                ", fromuserid=" + fromuserid +
                ", groupid=" + groupid +
                ", amount=" + amount +
                '}';
    }
}
