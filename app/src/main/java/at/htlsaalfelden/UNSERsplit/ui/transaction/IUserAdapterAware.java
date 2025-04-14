package at.htlsaalfelden.UNSERsplit.ui.transaction;

import at.htlsaalfelden.UNSERsplit.NoLib.Observable;

public interface IUserAdapterAware {
    Observable<Boolean> getIsSplitEven();
    Observable<Boolean> getDeleteMode();
    void onUserChange();
}
