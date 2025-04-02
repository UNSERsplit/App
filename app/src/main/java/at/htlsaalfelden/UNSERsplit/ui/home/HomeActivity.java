package at.htlsaalfelden.UNSERsplit.ui.home;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedGroup;
import at.htlsaalfelden.UNSERsplit.api.model.Group;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.api.model.Transaction;
import at.htlsaalfelden.UNSERsplit.ui.settings.SettingsActivity;
import at.htlsaalfelden.UNSERsplit.ui.transaction.TransactionActivity;

public class HomeActivity extends AppCompatActivity {


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);


        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        findViewById(R.id.navAdd).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, TransactionActivity.class);
            startActivity(myIntent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        findViewById(R.id.navSettings).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        ListView groupsView = findViewById(R.id.listVgruppenPersonen);
        //groups.setAdapter(new ArrayAdapter<String>(this, R.layout.layout_group, R.id.txtViewGroupName, new String[]{"a","b","c","d","" +
        //        "e","f","g","h","i","j","k","l","m","n"}));

        List<CombinedGroup> combinedGroups = new ArrayList<>();

        BaseAdapter adapter = new GroupAdapter(this, combinedGroups);

        groupsView.setAdapter(adapter);

        API.service.getGroups().enqueue((DefaultCallback<List<Group>>) groups -> {
            Logger.getLogger("UNSERSPLIT").info("groups");
            assert groups != null;
            for(Group group : groups) {
                Logger.getLogger("UNSERSPLIT").info("group " + group.getGroupid());
                CombinedGroup combinedGroup = new CombinedGroup(
                        group,
                        List.of(),
                        0
                );
                combinedGroups.add(combinedGroup);
                adapter.notifyDataSetChanged();

                API.service.getUsers(group.getGroupid()).enqueue((DefaultCallback<List<PublicUserData>>) users -> {
                    Logger.getLogger("UNSERSPLIT").info("users in group " + group.getGroupid());
                    combinedGroup.setMembers(users);
                    adapter.notifyDataSetChanged();
                });

                API.service.getTransactions().enqueue((DefaultCallback<List<Transaction>>) transactions -> {
                    Logger.getLogger("UNSERSPLIT").info("transactions in group " + group.getGroupid());
                    int balance = 0;

                    assert transactions != null;
                    for (Transaction transaction : transactions) {
                        if(transaction.getGroupid() != group.getGroupid()) {
                            continue;
                        }

                        if(transaction.getFromuserid() == API.userID) {
                            balance -= transaction.getAmount();
                        } else if(transaction.getTouserid() == API.userID) {
                            balance += transaction.getAmount();
                        }
                    }
                    combinedGroup.setBalance(balance);
                    adapter.notifyDataSetChanged();
                });
            }

        });


        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}