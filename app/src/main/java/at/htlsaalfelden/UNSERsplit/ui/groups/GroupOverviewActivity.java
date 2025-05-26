package at.htlsaalfelden.UNSERsplit.ui.groups;

import static at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils.get;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import at.htlsaalfelden.UNSERsplit.NoLib.Observable;
import at.htlsaalfelden.UNSERsplit.NoLib.ui.LayoutSwitcher;
import at.htlsaalfelden.UNSERsplit.NoLib.ui.UserSearchView;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;
import at.htlsaalfelden.UNSERsplit.api.model.Group;
import at.htlsaalfelden.UNSERsplit.api.model.GroupCreateRequest;
import at.htlsaalfelden.UNSERsplit.api.model.GroupMembers;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.api.model.Transaction;
import at.htlsaalfelden.UNSERsplit.ui.NavigationUtils;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;
import at.htlsaalfelden.UNSERsplit.ui.transaction.IUserAdapterAware;
import at.htlsaalfelden.UNSERsplit.ui.transaction.UserAdapter;

public class GroupOverviewActivity extends AppCompatActivity {
    public final Observable<Boolean> showMembers = new Observable<>(false);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_overview);

        LayoutSwitcher layoutSwitcher = findViewById(R.id.memberSwitcher);

        layoutSwitcher.setStateVariable(showMembers);

        NavigationUtils.initNavbar(this );

        int groupId = getIntent().getIntExtra("GROUP", -1);
        showMembers.set(!getIntent().getBooleanExtra("EDITING", false));

        var ctx = this;

        //dynamic ContentContainer
        ConstraintLayout layout = findViewById(R.id.contentContainer);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height =  (int)(displayMetrics.heightPixels * 0.6);
        int width = (int) (displayMetrics.widthPixels * 0.80);

        params.height = height;
        params.width = width;
        layout.setLayoutParams(params);

        //Dynamic Contentlayout
        ConstraintLayout gruppenSettingsContainer = findViewById(R.id.gruppenSettingsContainer);
        ViewGroup.LayoutParams params2 = gruppenSettingsContainer.getLayoutParams();
        DisplayMetrics displayMetrics2 = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics2);
        height =  (int)(displayMetrics2.heightPixels * 0.57);

        params2.height = height;

        gruppenSettingsContainer.setLayoutParams(params2);

        //Dynamic mitgliederContainer
        ConstraintLayout mitgliederContainer = findViewById(R.id.mitgliederContainer);
        ViewGroup.LayoutParams params3 = mitgliederContainer.getLayoutParams();
        DisplayMetrics displayMetrics3 = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics3);
        height =  (int)(displayMetrics3.heightPixels * 0.5);

        params3.height = height;

        mitgliederContainer.setLayoutParams(params3);

        ListView mitgliederList = findViewById(R.id.mitgliederList);


        List<CombinedUser> users = new ArrayList<>();
        final List<PublicUserData>[] originalUsers = new List[]{null};

        UserAdapter settingsUserAdapter = new UserAdapter(new StaticAwareContext(false, true), users, this);
        UserAdapter normalUserAdapter = new NormalUserAdapter(new StaticAwareContext(true, false), users, this);

        mitgliederList.setAdapter(normalUserAdapter);

        TextView groupName = findViewById(R.id.textViewGruppenName);
        EditText groupNameEdit = findViewById(R.id.txtViewSettingVornameData);
        TextView groupInfo = findViewById(R.id.textViewInfo);

        API.service.getGroup(groupId).enqueue(new DefaultCallback<Group>() {
            @Override
            public void onSucess(@Nullable Group response) {
                groupName.setText(response.getName());
                groupNameEdit.setText(response.getName());

                if(response.getAdminuser_userid() != API.userID) {
                    findViewById(R.id.memberSwitcher).setVisibility(View.INVISIBLE);
                }
            }
        });

        API.service.getUsers(groupId).enqueue(new DefaultCallback<List<PublicUserData>>() {
            @Override
            public void onSucess(@Nullable List<PublicUserData> response) {
                AtomicInteger i = new AtomicInteger(0);
                for(PublicUserData userData : response) {
                    System.out.println(userData.getUserid() + " " + userData.getFirstname() + " " + API.userID);
                    if(userData.getUserid() == API.userID) {
                        continue;
                    }
                    i.addAndGet(1);
                    API.service.getTransactions(userData.getUserid()).enqueue(new DefaultCallback<List<Transaction>>() {
                        @Override
                        public void onSucess(@Nullable List<Transaction> response2) {
                            double balance = 0;

                            assert response2 != null;
                            for (Transaction transaction : response2) {
                                if(transaction.getGroupid() == null || transaction.getGroupid() != groupId) {
                                    continue;
                                }

                                if(transaction.getFromuserid() == API.userID) {
                                    balance -= transaction.getAmount();
                                }
                                if(transaction.getTouserid() == API.userID) {
                                    balance += transaction.getAmount();
                                }
                            }

                            users.add(new CombinedUser(userData, balance));

                            if(i.addAndGet(-1) == 0) {
                                normalUserAdapter.notifyDataSetChanged();
                                settingsUserAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

                groupInfo.setText("Mitglieder: " + response.size());

                originalUsers[0] = response;

                if(response.size() == 1) {
                    findViewById(R.id.looksEmptyHereView).setVisibility(View.VISIBLE);
                    findViewById(R.id.mitgliederList).setVisibility(View.GONE);
                }
            }
        });


        showMembers.addInstantListener((o,v) -> {
            if(!v) {
                mitgliederContainer.setVisibility(View.INVISIBLE);
                gruppenSettingsContainer.setVisibility(View.VISIBLE);
            } else {
                mitgliederContainer.setVisibility(View.VISIBLE);
                gruppenSettingsContainer.setVisibility(View.INVISIBLE);
            }
        });


        findViewById(R.id.btnLoeschen).setOnClickListener(v ->{
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        findViewById(R.id.btnSpeichern).setOnClickListener(v -> {
            AtomicInteger a = new AtomicInteger(0);
            List<CombinedUser> usersToAdd = new ArrayList<>(users);
            List<PublicUserData> usersToRemove = new ArrayList<>(originalUsers[0]);

            for(PublicUserData user : originalUsers[0]) {
                usersToAdd.removeIf((u) -> u.getUserData().getUserid() == user.getUserid());
            }

            for(CombinedUser user : users) {
                usersToRemove.removeIf((u) -> u.getUserid() == user.getUserData().getUserid());
            }

            usersToRemove.removeIf(u -> u.getUserid() == API.userID);

            int t = usersToRemove.size() + usersToAdd.size() + 1;

            API.service.updateGroup(groupId, new GroupCreateRequest(groupNameEdit.getText().toString())).enqueue(new DefaultCallback<Group>() {
                @Override
                public void onSucess(@Nullable Group response) {
                    if(a.addAndGet(1) == t) {
                        NavigationUtils.reloadSelf(ctx);
                    }
                }
            });



            for(CombinedUser user : usersToAdd) {
                API.service.inviteUser(groupId, user.getUserData().getUserid()).enqueue(new DefaultCallback<GroupMembers>() {
                    @Override
                    public void onSucess(@Nullable GroupMembers response) {
                        API.service.addUser(groupId, user.getUserData().getUserid()).enqueue(new DefaultCallback<GroupMembers>() {
                            @Override
                            public void onSucess(@Nullable GroupMembers response) {
                                if(a.addAndGet(1) == t) {
                                    NavigationUtils.reloadSelf(ctx);
                                }
                            }
                        });
                    }
                });
            }

            for(PublicUserData user : usersToRemove) {
                API.service.removeUser(groupId, user.getUserid()).enqueue(new DefaultCallback<GroupMembers>() {
                    @Override
                    public void onSucess(@Nullable GroupMembers response) {
                        if(a.addAndGet(1) == t) {
                            NavigationUtils.reloadSelf(ctx);
                        }
                    }
                });
            }
        });


        ListView listView = findViewById(R.id.mitgliederListe);
        listView.setAdapter(settingsUserAdapter);

        UserSearchView searchView = findViewById(R.id.searchViewAddPersonToGroup);

        searchView.setOnEntrySelect(publicUserData -> {
            searchView.setQuery("", false);

            CombinedUser user = new CombinedUser(publicUserData, 1);
            user.setAdapter(normalUserAdapter);
            users.add(user);

            onUserAdd(user);
            normalUserAdapter.notifyDataSetChanged();
        });
    }

    private void onUserAdd(CombinedUser user) {

    }



    public static class StaticAwareContext implements IUserAdapterAware {
        private Observable<Boolean> even;
        private Observable<Boolean> delete;

        public StaticAwareContext(boolean isSplitEven, boolean deleteMode) {
            this.even = new Observable<>(isSplitEven);
            this.delete = new Observable<>(deleteMode);
        }
        @Override
        public Observable<Boolean> getIsSplitEven() {
            return this.even;
        }

        @Override
        public Observable<Boolean> getDeleteMode() {
            return this.delete;
        }

        @Override
        public void onUserChange() {

        }
    }


}
