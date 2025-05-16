package at.htlsaalfelden.UNSERsplit.ui.home;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

public class HomeActivity extends AppCompatActivity {


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
        int height =  (int)(displayMetrics.heightPixels * 0.54);
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

        findViewById(R.id.buttonAddGroups).setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
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