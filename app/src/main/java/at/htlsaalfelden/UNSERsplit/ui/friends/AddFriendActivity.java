package at.htlsaalfelden.UNSERsplit.ui.friends;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import at.htlsaalfelden.UNSERsplit.NoLib.Observable;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedFriend;
import at.htlsaalfelden.UNSERsplit.api.model.FriendData;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.ui.NavigationUtils;


public class AddFriendActivity extends AppCompatActivity {
    private Observable<Boolean> showPending = new Observable<>(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_friend);

        NavigationUtils.initScreen(this);

        var ctx = this;

        ConstraintLayout addList = findViewById(R.id.addList);
        ConstraintLayout requestList = findViewById(R.id.requestList);

        findViewById(R.id.textView4).setOnClickListener((v)->{
            showPending.set(false);
        });

        findViewById(R.id.textView7).setOnClickListener((v)->{
            showPending.set(true);
        });

        showPending.addInstantListener((o,v)->{
            if(v) {
                addList.setVisibility(View.GONE);
                requestList.setVisibility(View.VISIBLE);
            } else {
                requestList.setVisibility(View.GONE);
                addList.setVisibility(View.VISIBLE);
            }
        });

        ListView friendRequestList = findViewById(R.id.friendRequestList);
        ListView friendsList = findViewById(R.id.friendsList);


        List<CombinedFriend> pendingFriends = new ArrayList<>();
        FriendAdapter pendingFriendAdapter = new FriendAdapter(AddFriendActivity.this, pendingFriends, true);
        friendRequestList.setAdapter(pendingFriendAdapter);

        List<CombinedFriend> activeFriends = new ArrayList<>();
        FriendAdapter friendListAdapter = new FriendAdapter(AddFriendActivity.this, activeFriends, false);
        friendsList.setAdapter(friendListAdapter);


        API.service.getPendingFriends().enqueue(new DefaultCallback<List<FriendData>>() {
            @Override
            public void onSucess(@Nullable List<FriendData> response) {
                for (FriendData friendData : response) {
                    int otherID = friendData.getInvited_userid();
                    if(otherID == API.userID) {
                        otherID = friendData.getInviting_userid();
                    }

                    API.service.getUser(otherID).enqueue(new DefaultCallback<PublicUserData>() {
                        @Override
                        public void onSucess(@Nullable PublicUserData response) {
                            pendingFriends.add(new CombinedFriend(response, friendData));
                            pendingFriendAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        API.service.getActiveFriends().enqueue(new DefaultCallback<List<FriendData>>() {
            @Override
            public void onSucess(@Nullable List<FriendData> response) {
                for (FriendData friendData : response) {
                    int otherID = friendData.getInvited_userid();
                    if(otherID == API.userID) {
                        otherID = friendData.getInviting_userid();
                    }

                    API.service.getUser(otherID).enqueue(new DefaultCallback<PublicUserData>() {
                        @Override
                        public void onSucess(@Nullable PublicUserData response) {
                            activeFriends.add(new CombinedFriend(response, friendData));
                            friendListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });


    }
}
