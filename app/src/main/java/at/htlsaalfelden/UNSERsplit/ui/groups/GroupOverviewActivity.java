package at.htlsaalfelden.UNSERsplit.ui.groups;

import static at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils.get;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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
import at.htlsaalfelden.UNSERsplit.api.model.CombinedGroup;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;
import at.htlsaalfelden.UNSERsplit.api.model.Group;
import at.htlsaalfelden.UNSERsplit.api.model.GroupCreateRequest;
import at.htlsaalfelden.UNSERsplit.api.model.GroupMembers;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.api.model.User;
import at.htlsaalfelden.UNSERsplit.ui.NavigationUtils;
import at.htlsaalfelden.UNSERsplit.ui.register.RegisterActivity;
import at.htlsaalfelden.UNSERsplit.ui.transaction.IUserAdapterAware;
import at.htlsaalfelden.UNSERsplit.ui.transaction.UserAdapter;

public class GroupOverviewActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_overview);

        NavigationUtils.initNavbar(this );

        int groupId = getIntent().getIntExtra("GROUP", -1);

        var ctx = this;

        //dynamic ContentContainer
        ConstraintLayout layout = findViewById(R.id.contentContainer);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height =  (int)(displayMetrics.heightPixels * 0.62);
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
        ViewGroup.LayoutParams params3 = gruppenSettingsContainer.getLayoutParams();
        DisplayMetrics displayMetrics3 = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics3);
        height =  (int)(displayMetrics3.heightPixels * 0.57);

        params2.height = height;

        mitgliederContainer.setLayoutParams(params2);

        List<CombinedUser> users = new ArrayList<>();
        final List<PublicUserData>[] originalUsers = new List[]{null};
        StaticAwareContext context = new StaticAwareContext(false, true);
        UserAdapter settingsUserAdapter = new UserAdapter(context, users, this);

        TextView groupName = findViewById(R.id.textViewGruppenName);
        EditText groupNameEdit = findViewById(R.id.txtViewSettingVornameData);
        TextView groupInfo = findViewById(R.id.textViewInfo);

        API.service.getGroup(groupId).enqueue(new DefaultCallback<Group>() {
            @Override
            public void onSucess(@Nullable Group response) {
                groupName.setText(response.getName());
                groupNameEdit.setText(response.getName());
            }
        });

        API.service.getUsers(groupId).enqueue(new DefaultCallback<List<PublicUserData>>() {
            @Override
            public void onSucess(@Nullable List<PublicUserData> response) {
                for(PublicUserData userData : response) {
                    users.add(new CombinedUser(userData, 0));
                }

                groupInfo.setText("Mitglieder: " + response.size());

                settingsUserAdapter.notifyDataSetChanged();

                originalUsers[0] = response;
            }
        });


        findViewById(R.id.textViewMitglieder).setOnClickListener(v -> {
                mitgliederContainer.setVisibility(View.VISIBLE);
                gruppenSettingsContainer.setVisibility(View.INVISIBLE);
        });

        findViewById(R.id.textViewSettings).setOnClickListener(v -> {
            mitgliederContainer.setVisibility(View.INVISIBLE);
            gruppenSettingsContainer.setVisibility(View.VISIBLE);
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

        SearchView searchView = findViewById(R.id.searchViewAddPersonToGroup);

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.layout_username, null, new String[] {SearchManager.SUGGEST_COLUMN_TEXT_1}, new int[] {R.id.txtViewUsername}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.onActionViewExpanded();

        getSearchAutoComplete(searchView).setThreshold(2); // Bei 1 Funktioniert der Divider nicht


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MatrixCursor cursor = new MatrixCursor(new String[]{"_id", SearchManager.SUGGEST_COLUMN_TEXT_1});

                API.service.searchUser(newText).enqueue(new DefaultCallback<List<PublicUserData>>() {
                    @Override
                    public void onSucess(@Nullable List<PublicUserData> response) {
                        for(PublicUserData publicUserData : response) {
                            cursor.addRow(new Object[] {publicUserData.getUserid(), publicUserData.getFirstname() + " " + publicUserData.getLastname()});
                        }

                        cursorAdapter.changeCursor(cursor);
                        cursorAdapter.notifyDataSetChanged();

                        ListView suggestionsView = getListViewUnsafe(searchView);

                        if(suggestionsView != null) {
                            suggestionsView.setDivider(new ColorDrawable(0xff828282));
                            suggestionsView.setDividerHeight(7);
                        }
                    }
                });

                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                @SuppressLint("Range") String selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
                @SuppressLint("Range") int userid = cursor.getInt(cursor.getColumnIndex("_id"));

                searchView.setQuery("", false);

                API.service.getUser(userid).enqueue(new DefaultCallback<PublicUserData>() {
                    @Override
                    public void onSucess(@Nullable PublicUserData response) {
                        CombinedUser user = new CombinedUser(response, 1);
                        user.setAdapter(settingsUserAdapter);
                        users.add(user);

                        onUserAdd(user);

                        settingsUserAdapter.notifyDataSetChanged();
                    }
                });

                return true;
            }
        });
    }

    private static ListView getListViewUnsafe(SearchView searchView) {
        Object searchAutoComplete = get(searchView, "mSearchSrcTextView");

        //showMembers(searchAutoComplete);

        ListPopupWindow popupWindow = get(searchAutoComplete, "mPopup", AutoCompleteTextView.class);
        return get(popupWindow, "mDropDownList");
    }

    private static AutoCompleteTextView getSearchAutoComplete(SearchView searchView) {
        AutoCompleteTextView searchAutoComplete = get(searchView, "mSearchSrcTextView");

        return searchAutoComplete;
    }

    private void onUserAdd(CombinedUser user) {

    }



    private static class StaticAwareContext implements IUserAdapterAware {
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
