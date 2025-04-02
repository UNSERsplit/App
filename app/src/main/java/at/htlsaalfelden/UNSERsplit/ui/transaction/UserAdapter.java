package at.htlsaalfelden.UNSERsplit.ui.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedGroup;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private List<CombinedUser> users;

    private LayoutInflater layoutInflater;


    public UserAdapter(@NonNull Context context, @NonNull List<CombinedUser> groups) {
        this.context = context;
        this.users = groups;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public CombinedUser getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(layoutInflater, position, convertView, parent, R.layout.layout_transaction_userlist_half);
    }

    private @NonNull View createViewFromResource(@NonNull LayoutInflater inflater, int position,
                                                 @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
        final View view;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        final CombinedUser item = getItem(position);

        TextView username = view.findViewById(R.id.txtViewBenutzername);
        TextView betrag = view.findViewById(R.id.textViewBetrag);
        EditText betragEdit = view.findViewById(R.id.editTextBetrag);

        username.setText(item.getUserData().getFirstname() + " " + item.getUserData().getLastname());
        betrag.setText(item.getBalance() + "");
        betragEdit.setText("");

        return view;
    }
}
