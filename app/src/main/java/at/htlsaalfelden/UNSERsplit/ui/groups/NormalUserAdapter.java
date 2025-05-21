package at.htlsaalfelden.UNSERsplit.ui.groups;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.ui.IBANutils;
import at.htlsaalfelden.UNSERsplit.ui.transaction.IUserAdapterAware;
import at.htlsaalfelden.UNSERsplit.ui.transaction.UserAdapter;

public class NormalUserAdapter extends UserAdapter {
    public NormalUserAdapter(@NonNull IUserAdapterAware context, @NonNull List<CombinedUser> groups) {
        super(context, groups);
    }

    public NormalUserAdapter(@NonNull IUserAdapterAware userContext, @NonNull List<CombinedUser> groups, @NonNull AppCompatActivity activity) {
        super(userContext, groups, activity);
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View o = super.getView(position, convertView, parent);

        CombinedUser data = getItem(position);

        o.setOnClickListener((v) -> {
            String iban = data.getUserData().getIban();

            if(!IBANutils.isValidIban(iban)) {
                Toast.makeText(this.activity, "no valid IBAN found", Toast.LENGTH_SHORT).show();

                return;
            }

            ClipboardManager clipboard = (ClipboardManager) this.activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(iban, iban);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this.activity, "copied IBAN", Toast.LENGTH_SHORT).show();
        });

        return o;
    }
}
