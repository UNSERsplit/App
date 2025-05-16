package at.htlsaalfelden.UNSERsplit.ui.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedFriend;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;
import at.htlsaalfelden.UNSERsplit.api.model.FriendData;
import at.htlsaalfelden.UNSERsplit.ui.transaction.IUserAdapterAware;

public class FriendAdapter extends BaseAdapter {
    private List<CombinedFriend> friends;
    private AddFriendActivity context;
    private LayoutInflater layoutInflater;
    private boolean pending;

    public FriendAdapter(@NonNull AddFriendActivity context, @NonNull List<CombinedFriend> friends, boolean isPending) {
        this.context = context;
        this.friends = friends;
        this.layoutInflater = LayoutInflater.from((AppCompatActivity) context);
        this.pending = isPending;
    }

    @Override
    public int getCount() {
        return this.friends.size();
    }

    @Override
    public CombinedFriend getItem(int position) {
        return this.friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(layoutInflater, position, convertView, parent, R.layout.layout_pending_friend);
    }

    private @NonNull View createViewFromResource(@NonNull LayoutInflater inflater, int position,
                                                 @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
        final View view;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        final CombinedFriend item = getItem(position);

        TextView textViewFriendName = view.findViewById(R.id.txtViewFriendName);
        textViewFriendName.setText(item.getUserData().getFirstname() + " " + item.getUserData().getLastname());

        FloatingActionButton buttonAccept = view.findViewById(R.id.buttonAccept);
        FloatingActionButton buttonDeny = view.findViewById(R.id.buttonDeny);

        if(this.pending) {
            buttonAccept.setOnClickListener((v)->{
                API.service.acceptFriendRequest(item.getFriendData().getId()).enqueue(new DefaultCallback<FriendData>() {
                    @Override
                    public void onSucess(@Nullable FriendData response) {
                        friends.remove(position);
                        notifyDataSetChanged();

                        context.onFriendConsumer.accept(item);
                    }
                });
            });
            buttonDeny.setOnClickListener((v)->{
                API.service.denyFriendRequest(item.getFriendData().getId()).enqueue(new DefaultCallback<FriendData>() {
                    @Override
                    public void onSucess(@Nullable FriendData response) {
                        friends.remove(position);
                        notifyDataSetChanged();
                    }
                });
            });
        } else {
            buttonAccept.setVisibility(View.GONE);
            buttonDeny.setOnClickListener((v)->{
                int otherId = item.getFriendData().getInvited_userid();
                if(otherId == API.userID) {
                    otherId = item.getFriendData().getInviting_userid();
                }
                API.service.removeFriend(otherId).enqueue(new DefaultCallback<FriendData>() {
                    @Override
                    public void onSucess(@Nullable FriendData response) {
                        friends.remove(position);
                        notifyDataSetChanged();
                    }
                });
            });
            //buttonDeny.setText("Entfernen");
        }

        return view;
    }
}
