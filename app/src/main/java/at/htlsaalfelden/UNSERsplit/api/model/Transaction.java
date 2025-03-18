package at.htlsaalfelden.UNSERsplit.api.model;

public class Transaction {
    private int touserid;
    private int amount;
    private Integer groupid;
    private int transactionid;
    private int fromuserid;
    private String date;

    public int getTouserid() {
        return touserid;
    }

    public int getAmount() {
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
}
