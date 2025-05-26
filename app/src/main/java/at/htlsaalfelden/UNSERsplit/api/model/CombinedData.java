package at.htlsaalfelden.UNSERsplit.api.model;

import android.content.Context;
import android.content.Intent;

public interface CombinedData {
    String getName();
    double getBalance();
    String getExtra(Context context);
    Intent getClickIntent(Context ctx);

    int getCombinedId();
}
