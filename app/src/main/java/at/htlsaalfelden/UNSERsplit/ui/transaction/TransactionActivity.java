package at.htlsaalfelden.UNSERsplit.ui.transaction;

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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;
import at.htlsaalfelden.UNSERsplit.ui.settings.SettingsActivity;


public class TransactionActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);

        AtomicBoolean isSplitEven = new AtomicBoolean(true);

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
            TextView a = findViewById(R.id.textViewBetrag);
            a.setVisibility(View.VISIBLE);
            EditText b = findViewById(R.id.editTextNumber);
            b.setVisibility(View.GONE);
            isSplitEven.set(true);
        });

        findViewById(R.id.textViewbtnCostum).setOnClickListener(v -> {
            TextView a = findViewById(R.id.textViewBetrag);
            a.setVisibility(View.GONE);
            EditText b = findViewById(R.id.editTextNumber);
            b.setVisibility(View.VISIBLE);
            isSplitEven.set(false);
        });

        List<CombinedUser> users = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(this, users);

        ListView listView = findViewById(R.id.scrollView2);
        listView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchViewAddPersonen);

        EditText editTextBetrag = findViewById(R.id.editTextBetrag);

        final double[] sum = {0};

        editTextBetrag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                sum[0] = Double.parseDouble(editTextBetrag.getText().toString());
                modifyBalances(isSplitEven, users, adapter, sum[0]);
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
                        user.setAdapter(adapter);
                        users.add(user);

                        modifyBalances(isSplitEven, users, adapter, sum[0]);
                    }
                });

                return true;
            }
        });
    }

    private void modifyBalances(AtomicBoolean isSplitEven, List<CombinedUser> users, BaseAdapter userAdapter, double sum) {
        if(isSplitEven.get()) {
            double per_user = Math.ceil((sum / users.size()) * 100) / 100; // maximal 2 Nachkommastellen
            for(CombinedUser combinedUser : users) {
                combinedUser.setBalance(per_user);
            }

            userAdapter.notifyDataSetChanged();
        }
    }

    private static ListView getListViewUnsafe(SearchView searchView) {
        Object searchAutoComplete = get(searchView, "mSearchSrcTextView");

        //showMembers(searchAutoComplete);

        ListPopupWindow popupWindow = get(searchAutoComplete, "mPopup", AutoCompleteTextView.class);
        return get(popupWindow, "mDropDownList");
    }

    private static <T> T get(Object o, String name) {
        return get(o,name,o.getClass());
    }

    private static <T> T get(Object o, String name, Class<?> c) {
        try {
            Field field = c.getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showMembers(Class<?> c) {
        System.out.println("Logging for Class '" + c.getSimpleName() + "'");
        System.out.println("Fields: ");
        for(Field field : c.getDeclaredFields()) {
            System.out.println("\t" + field.getName() + " : " + field.getType().getSimpleName());
        }
        System.out.println("Methods: ");
        for(Method m : c.getDeclaredMethods()) {
            System.out.println("\t" + m.getName() + " : " + m.getReturnType().getSimpleName());
        }
        if(c.getSuperclass() != null && !Objects.equals(c.getSuperclass(), Object.class)) {
            showMembers(c.getSuperclass());
        }
    }

    private static void showMembers(Object o) {
        showMembers(o.getClass());
    }
}
