package at.htlsaalfelden.UNSERsplit.api.model;

public class TransactionCreateRequest {
    private int touserid;
    private double amount;
    private Integer groupid;

    public int getTouserid() {
        return touserid;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public TransactionCreateRequest(int touserid, double amount, Integer groupid) {
        this.touserid = touserid;
        this.amount = amount;
        this.groupid = groupid;
    }
}
