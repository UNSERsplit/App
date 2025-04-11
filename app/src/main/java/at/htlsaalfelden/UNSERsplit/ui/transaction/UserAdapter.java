package at.htlsaalfelden.UNSERsplit.ui.transaction;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

    private TransactionActivity context;
    private List<CombinedUser> users;

    private LayoutInflater layoutInflater;


    public UserAdapter(@NonNull TransactionActivity context, @NonNull List<CombinedUser> groups) {
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
        TextView username2 = view.findViewById(R.id.txtViewBenutzername2);
        TextView betrag = view.findViewById(R.id.textViewBetrag);
        EditText betragEdit = view.findViewById(R.id.editTextNumber);

        username.setText(item.getUserData().getFirstname() + " " + item.getUserData().getLastname());
        username2.setText(item.getUserData().getFirstname() + " " + item.getUserData().getLastname());
        betrag.setText(item.getBalance() + "");
        if(!betragEdit.getText().toString().equals(item.getBalance() + "")) {
            betragEdit.setText(item.getBalance() + "");
        }

        context.isSplitEven.addInstantListener((o,v) -> {
            if(v) {
                betrag.setVisibility(View.VISIBLE);
                betragEdit.setVisibility(View.INVISIBLE);
            } else {
                betrag.setVisibility(View.INVISIBLE);
                betragEdit.setVisibility(View.VISIBLE);
            }
        });

        context.deleteMode.addInstantListener((o,v)-> {
            if(v) {
                view.findViewById(R.id.linearLayout_default).setVisibility(View.GONE);
                view.findViewById(R.id.linearLayout_delete).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.linearLayout_default).setVisibility(View.VISIBLE);
                view.findViewById(R.id.linearLayout_delete).setVisibility(View.GONE);
            }
        });

        view.findViewById(R.id.floatingActionButton).setOnClickListener(v -> {
            this.users.remove(item);
            context.onUserChange();
            this.notifyDataSetChanged();
        });

        betragEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Double.parseDouble(betragEdit.getText().toString());
                } catch (NumberFormatException e) {
                    item.setBalanceNoNotify(0);
                    context.onUserChange();
                    return;
                }
                item.setBalanceNoNotify(Math.ceil(Double.parseDouble(betragEdit.getText().toString()) * 100) / 100);
                context.onUserChange();
            }
        });

        return view;
    }
}
