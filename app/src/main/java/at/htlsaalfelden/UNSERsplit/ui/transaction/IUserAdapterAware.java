package at.htlsaalfelden.UNSERsplit.ui.transaction;

import at.htlsaalfelden.UNSERsplit.NoLib.Observable;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;

public interface IUserAdapterAware {
    Observable<Boolean> getIsSplitEven();
    Observable<Boolean> getDeleteMode();
    void onUserChange(CombinedUser user);
}
