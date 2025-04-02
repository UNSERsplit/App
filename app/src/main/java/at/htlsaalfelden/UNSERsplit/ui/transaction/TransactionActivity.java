package at.htlsaalfelden.UNSERsplit.ui.transaction;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

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
        });

        findViewById(R.id.textViewbtnCostum).setOnClickListener(v -> {
            TextView a = findViewById(R.id.textViewBetrag);
            a.setVisibility(View.GONE);
            EditText b = findViewById(R.id.editTextNumber);
            b.setVisibility(View.VISIBLE);
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

        List<CombinedUser> users = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(this, users);

        ListView listView = findViewById(R.id.scrollView2);
        listView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchViewAddPersonen);



        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.layout_transaction_userlist_half, null, new String[] {SearchManager.SUGGEST_COLUMN_TEXT_1}, new int[] {R.id.txtViewBenutzername}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

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
                    }
                });

                return true;
            }
        });
    }
}
