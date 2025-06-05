package at.htlsaalfelden.UNSERsplit.ui.friends;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import at.htlsaalfelden.UNSERsplit.NoLib.Observable;
import at.htlsaalfelden.UNSERsplit.NoLib.ui.LayoutSwitcher;
import at.htlsaalfelden.UNSERsplit.NoLib.ui.UserSearchView;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.FailableCallback;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedFriend;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;
import at.htlsaalfelden.UNSERsplit.api.model.FriendData;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.ui.IBANutils;
import at.htlsaalfelden.UNSERsplit.ui.NavigationUtils;
import retrofit2.Call;
import retrofit2.Response;


public class AddFriendActivity extends AppCompatActivity {
    private Observable<Boolean> showActive = new Observable<>(true);
    public Consumer<CombinedFriend> onFriendConsumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_friend);
        NavigationUtils.initNavbar(this);

        var ctx = this;

        LayoutSwitcher layoutSwitcher = findViewById(R.id.layoutSwitcher);
        layoutSwitcher.setStateVariable(showActive);

        showActive.set(!getIntent().getBooleanExtra("PENDING", false));

        ConstraintLayout addList = findViewById(R.id.addList);
        ConstraintLayout requestList = findViewById(R.id.requestList);


        ListView friendRequestList = findViewById(R.id.friendRequestList);
        ListView friendsList = findViewById(R.id.friendsList);


        List<CombinedFriend> pendingFriends = new ArrayList<>();
        FriendAdapter pendingFriendAdapter = new FriendAdapter(AddFriendActivity.this, pendingFriends, true);
        friendRequestList.setAdapter(pendingFriendAdapter);

        List<CombinedFriend> activeFriends = new ArrayList<>();
        FriendAdapter friendListAdapter = new FriendAdapter(AddFriendActivity.this, activeFriends, false);
        friendsList.setAdapter(friendListAdapter);

        onFriendConsumer = new Consumer<CombinedFriend>() {
            @Override
            public void accept(CombinedFriend friendData) {
                activeFriends.add(friendData);
                friendListAdapter.notifyDataSetChanged();
            }
        };


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

        UserSearchView userSearchView = findViewById(R.id.addFriendSearch);
        userSearchView.setOnEntrySelect(publicUserData -> {
            if(publicUserData.getUserid() == API.userID) {
                return;
            }

            if(activeFriends.stream().anyMatch((f) -> f.getUserData().getUserid() == publicUserData.getUserid())) {
                return;
            }

            API.service.sendFriendRequest(publicUserData.getUserid()).enqueue(new FailableCallback<>() {
                @Override
                public void on400(@NonNull Call<FriendData> call, @NonNull Response<FriendData> response, Object requestData) {
                    if(response.code() == 406) {
                        Toast.makeText(ctx, ctx.getText(R.string.already_friend), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    FailableCallback.super.on400(call, response, requestData);
                }

                @Override
                public void onSucess(@Nullable FriendData response) {

                }
            });
        });
    }

    public void dynamicSize(View x, double heighta, double widtha){
        ViewGroup.LayoutParams params1 = x.getLayoutParams();
        DisplayMetrics displayMetrics1 = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics1);
        int height =  (int)(displayMetrics1.heightPixels * heighta);
        int width = (int) (displayMetrics1.widthPixels * widtha);

        params1.height = height;
        params1.width = width;
        x.setLayoutParams(params1);
    }
}
