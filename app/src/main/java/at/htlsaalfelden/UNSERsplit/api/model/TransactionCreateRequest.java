package at.htlsaalfelden.UNSERsplit.api.model;

public class TransactionCreateRequest {
    private int touserid;
    private float amount;
    private Integer groupid;

    public int getTouserid() {
        return touserid;
    }

    public float getAmount() {
        return amount;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public TransactionCreateRequest(int touserid, float amount, Integer groupid) {
        this.touserid = touserid;
        this.amount = amount;
        this.groupid = groupid;
    }
}
