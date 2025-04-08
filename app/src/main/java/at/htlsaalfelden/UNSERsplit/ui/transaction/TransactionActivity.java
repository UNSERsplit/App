package at.htlsaalfelden.UNSERsplit.ui.transaction;

import static at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils.call;
import static at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils.get;
import static at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils.showMembers;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import at.htlsaalfelden.UNSERsplit.NoLib.Observable;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;
import at.htlsaalfelden.UNSERsplit.ui.settings.SettingsActivity;


public class TransactionActivity extends AppCompatActivity {

    public Observable<Boolean> isSplitEven;
    public Observable<Double> totalSum;

    private List<CombinedUser> users;
    private UserAdapter userAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);

        isSplitEven = new Observable<>(true);
        totalSum = new Observable<>(0.0);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent);
        });

        findViewById(R.id.navAdd).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, TransactionActivity.class);
            startActivity(myIntent);
        });

        findViewById(R.id.navSettings).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
        });

        findViewById(R.id.textViewbtnHalf).setOnClickListener(v -> {
            isSplitEven.set(true);
        });

        findViewById(R.id.textViewbtnCostum).setOnClickListener(v -> {
            isSplitEven.set(false);
        });

        users = new ArrayList<>();
        userAdapter = new UserAdapter(this, users);

        ListView listView = findViewById(R.id.scrollView2);
        listView.setAdapter(userAdapter);

        SearchView searchView = findViewById(R.id.searchViewAddPersonen);

        EditText editTextBetrag = findViewById(R.id.editTextBetrag);

        totalSum.addListener((o,v) -> {
            if(this.isSplitEven.get()) {
                double per_user = Math.ceil((totalSum.get() / users.size()) * 100) / 100;
                for(CombinedUser u : this.users) {
                    u.setBalance(per_user);
                }
            }
        });

        isSplitEven.addListener((o,v) -> {
            if(v) {
                double per_user = Math.ceil((totalSum.get() / users.size()) * 100) / 100;
                for (CombinedUser u : this.users) {
                    u.setBalance(per_user);
                }
            }
        });

        editTextBetrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Double.parseDouble(editTextBetrag.getText().toString());
                } catch (NumberFormatException e) {
                    totalSum.set(0.0);
                    return;
                }

                totalSum.set(Double.parseDouble(editTextBetrag.getText().toString()));
            }
        });


        //Dynamic Iban
        /*ConstraintLayout layout = findViewById(R.id.addContainer);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height =  (int)(displayMetrics.heightPixels * 0.4);
        int width = (int) (displayMetrics.widthPixels * 0.75);

        displayMetrics.

        params.height = height;
        params.width = width;
        layout.setLayoutParams(params);*/






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
                        user.setAdapter(userAdapter);
                        users.add(user);

                        onUserAdd(user);
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
        if(this.isSplitEven.get()) {
            double per_user = Math.ceil((totalSum.get() / users.size()) * 100) / 100;
            for(CombinedUser u : this.users) {
                u.setBalance(per_user);
            }
        } else {
            double combined = 0;
            for(CombinedUser u : this.users) {
                combined += u.getBalance();
            }

            if(combined < this.totalSum.get()) {
                user.setBalance(this.totalSum.get() - combined);
            }
        }

        this.userAdapter.notifyDataSetChanged();
    }
}
