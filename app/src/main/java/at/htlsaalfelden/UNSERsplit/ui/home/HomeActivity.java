package at.htlsaalfelden.UNSERsplit.ui.home;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import at.htlsaalfelden.UNSERsplit.MainActivity;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedData;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedGroup;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;
import at.htlsaalfelden.UNSERsplit.api.model.Group;
import at.htlsaalfelden.UNSERsplit.api.model.GroupCreateRequest;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.api.model.Transaction;
import at.htlsaalfelden.UNSERsplit.ui.NavigationUtils;
import at.htlsaalfelden.UNSERsplit.ui.friends.AddFriendActivity;
import at.htlsaalfelden.UNSERsplit.ui.groups.GroupOverviewActivity;
import at.htlsaalfelden.UNSERsplit.ui.register.RegisterActivity;
import at.htlsaalfelden.UNSERsplit.ui.settings.SettingsActivity;
import at.htlsaalfelden.UNSERsplit.ui.transaction.TransactionActivity;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        NavigationUtils.initNavbar(this);

        setBalance(0);

        ListView groupsView = findViewById(R.id.listVgruppenPersonen);
        //groups.setAdapter(new ArrayAdapter<String>(this, R.layout.layout_group, R.id.txtViewGroupName, new String[]{"a","b","c","d","" +
        //"e","f","g","h","i","j","k","l","m","n"}));

        List<CombinedData> combinedGroups = new ArrayList<>();

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
                    double balance = 0;

                    assert transactions != null;
                    for (Transaction transaction : transactions) {
                        if(transaction.getGroupid() != group.getGroupid()) {
                            continue;
                        }

                        if(transaction.getFromuserid() == API.userID) {
                            balance -= transaction.getAmount();
                        }
                        if(transaction.getTouserid() == API.userID) {
                            balance += transaction.getAmount();
                        }
                    }
                    changeBalance(balance);
                    combinedGroup.setBalance(balance);
                    adapter.notifyDataSetChanged();
                });
            }

        });

        API.service.getFriends().enqueue(new DefaultCallback<List<PublicUserData>>() {
            @Override
            public void onSucess(@Nullable List<PublicUserData> response) {
                System.out.println(response);
                for(PublicUserData user : response) {
                    if(user.getUserid() == API.userID) {
                        continue;
                    }

                    CombinedUser combinedUser = new CombinedUser(user, 0);
                    combinedGroups.add(combinedUser);
                    adapter.notifyDataSetChanged();

                    API.service.getTransactions(user.getUserid()).enqueue(new DefaultCallback<List<Transaction>>() {
                        @Override
                        public void onSucess(@Nullable List<Transaction> response) {
                            double balance = 0;
                            double userBalance = 0;

                            for(Transaction transaction : response) {
                                if(transaction.getFromuserid() == API.userID) {
                                    balance -= transaction.getAmount();
                                    if(transaction.getGroupid() == null) {
                                        userBalance -= transaction.getAmount();
                                    }
                                }
                                if(transaction.getTouserid() == API.userID) {
                                    balance += transaction.getAmount();
                                    if(transaction.getGroupid() == null) {
                                        userBalance += transaction.getAmount();
                                    }
                                }
                            }
                            changeBalance(userBalance);
                            combinedUser.setBalanceNoNotify(balance);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });


        ConstraintLayout layout = findViewById(R.id.constraintLayout7);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height =  (int)(displayMetrics.heightPixels * 0.64);
        int width = (int) (displayMetrics.widthPixels * 0.8);

        params.height = height;
        params.width = width;
        layout.setLayoutParams(params);



        /*ConstraintLayout sumContainer = findViewById(R.id.sumContainer);
        ViewGroup.LayoutParams params2 = sumContainer.getLayoutParams();
        DisplayMetrics displayMetrics2 = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics2);
        int height2 =  (int)(displayMetrics2.heightPixels * 0.1);

        params2.height = height2;
        sumContainer.setLayoutParams(params2);*/

        findViewById(R.id.addFriends).setOnClickListener(v ->{
            Intent myIntent = new Intent(this, AddFriendActivity.class);
            startActivity(myIntent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        });


        //Side Navbar
        // Initialize the DrawerLayout, Toolbar, and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);




        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Create an ActionBarDrawerToggle to handle
        // the drawer's open/close state
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // gleiche Animation behalten
        toolbar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        // Add the toggle as a listener to the DrawerLayout
        drawerLayout.addDrawerListener(toggle);
        // Synchronize the toggle's state with the linked DrawerLayout
        toggle.syncState();

        // Set a listener for when an item in the NavigationView is selected
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // Called when an item in the NavigationView is selected.
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle the selected item based on its ID
                if (item.getItemId() == R.id.nav_settings) {
                    //switch to Settings
                        Intent myIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(myIntent,
                                ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());

                }

                if (item.getItemId() == R.id.nav_transaction) {
                    Intent myIntent = new Intent(HomeActivity.this, TransactionActivity.class);
                    startActivity(myIntent,
                            ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());

                }

                if (item.getItemId() == R.id.nav_createGroup) {
                    // Show a Toast message for the Logout item
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HomeActivity.this);
                    builder.setView(R.layout.popup_add_group);
                    final EditText[] input = {null};

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String groupName = input[0].getText().toString();

                            API.service.createGroup(new GroupCreateRequest(groupName)).enqueue(new DefaultCallback<Group>() {
                                @Override
                                public void onSucess(@Nullable Group response) {
                                    Intent myIntent = new Intent(HomeActivity.this, GroupOverviewActivity.class);
                                    myIntent.putExtra("GROUP", response.getGroupid());
                                    myIntent.putExtra("EDITING", true);
                                    startActivity(myIntent);
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    input[0] = dialog.findViewById(R.id.groupNameEditText);
                }

                if (item.getItemId() == R.id.navaddFriends) {
                    Intent myIntent = new Intent(HomeActivity.this, AddFriendActivity.class);
                    startActivity(myIntent,
                            ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                }

                if (item.getItemId() == R.id.nav_logout) {
                    API.setToken(HomeActivity.this ,"");
                    Intent myIntent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(myIntent,
                            ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this).toBundle());
                }

                // Close the drawer after selection
                drawerLayout.closeDrawers();
                // Indicate that the item selection has been handled
                return true;
            }
        });

        // Add a callback to handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            // Called when the back button is pressed.
            @Override
            public void handleOnBackPressed() {
                // Check if the drawer is open
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // Close the drawer if it's open
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Finish the activity if the drawer is closed
                    drawerLayout.openDrawer(GravityCompat.START);
                    //finish();
                }
            }
        });
    }

    private double getBalance() {
        TextView amount = findViewById(R.id.txtVkontostand);

        String b = (String) amount.getText();
        if(b.charAt(0) =='+') {
            return Double.parseDouble(b.substring(1));
        } else if (b.charAt(0) == '-') {
            return Double.parseDouble(b.substring(1)) * -1;
        }
        return 0;
    }

    private void setBalance(double newValue) {
        TextView amount = findViewById(R.id.txtVkontostand);

        String rtn = null;
        if (newValue < 0) {
            rtn = "" + newValue;
            amount.setText((CharSequence) rtn);
        }else {
            rtn = "+" + newValue;
            amount.setText((CharSequence) rtn);
        }
    }

    private void changeBalance(double delta) {
        setBalance(getBalance() + delta);
    }
}