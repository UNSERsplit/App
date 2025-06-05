package at.htlsaalfelden.UNSERsplit.ui.transaction;

import static at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils.call;
import static at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils.get;
import static at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils.showMembers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import at.htlsaalfelden.UNSERsplit.NoLib.Observable;
import at.htlsaalfelden.UNSERsplit.NoLib.ui.LayoutSwitcher;
import at.htlsaalfelden.UNSERsplit.NoLib.ui.SimpleAPISearchView;
import at.htlsaalfelden.UNSERsplit.NoLib.ui.UserAndGroupSearch;
import at.htlsaalfelden.UNSERsplit.NoLib.ui.UserSearchView;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.CombinedUser;
import at.htlsaalfelden.UNSERsplit.api.model.PublicUserData;
import at.htlsaalfelden.UNSERsplit.api.model.Transaction;
import at.htlsaalfelden.UNSERsplit.api.model.TransactionCreateRequest;
import at.htlsaalfelden.UNSERsplit.ui.NavigationUtils;


public class TransactionActivity extends AppCompatActivity implements IUserAdapterAware {

    public Observable<Boolean> isSplitEven;
    public Observable<Double> totalSum;
    public Observable<Boolean> deleteMode;

    private List<CombinedUser> users;
    private UserAdapter userAdapter;

    private Observable<Double> userSum;

    public Observable<Integer> groupId = new Observable<>(null);;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);

        NavigationUtils.initNavbar(this);

        isSplitEven = new Observable<>(true);
        totalSum = new Observable<>(0.0);
        deleteMode = new Observable<>(false);
        userSum = new Observable<>(0.0);

        LayoutSwitcher layoutSwitcher = findViewById(R.id.transactionSwitcher);
        layoutSwitcher.setStateVariable(isSplitEven);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);



        findViewById(R.id.btnLoeschen).setOnClickListener(v -> {
            deleteMode.set(!deleteMode.get());
        });

        users = new ArrayList<>();
        userAdapter = new UserAdapter(this, users);

        ListView listView = findViewById(R.id.scrollView2);
        listView.setAdapter(userAdapter);

        EditText editTextBetrag = findViewById(R.id.editTextBetrag);

        checkSum();

        totalSum.addListener((o,v) -> {
            if(this.isSplitEven.get()) {
                double per_user = Math.ceil((totalSum.get() / users.size()) * 100) / 100;
                for(CombinedUser u : this.users) {
                    u.setBalance(per_user);
                }
            }

            onUserChange(null);

            checkSum();
        });

        userSum.addListener((o,v) -> {
            checkSum();
        });

        isSplitEven.addListener((o,v) -> {
            if(v) {
                double per_user = Math.ceil((totalSum.get() / users.size()) * 100) / 100;
                for (CombinedUser u : this.users) {
                    u.setBalance(per_user);
                }
            }

            onUserChange(null);

            checkSum();
        });

        deleteMode.addListener((o,v) -> {
            Button btn = findViewById(R.id.btnLoeschen);

            if(v) {
                btn.setText(this.getString(R.string.button_back));
            } else {
                btn.setText(this.getString(R.string.button_delete));
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


        UserAndGroupSearch userSearchView = findViewById(R.id.searchViewAddPersonen);

        userSearchView.setActivity(this);

        userSearchView.setUserConsumer(publicUserData -> {
            userSearchView.setQuery("", false);

            if(users.stream().anyMatch((c) -> c.getUserData().getUserid() == publicUserData.getUserid())) {
                System.err.println(publicUserData.getUserid());
                return;
            } // sehr buggy



            CombinedUser user = new CombinedUser(publicUserData, 1);
            user.setAdapter(userAdapter);
            users.add(user);
            System.err.println("Added " + user.getUserData().getFirstname());

            onUserAdd(user);
        });

        userSearchView.setGroupConsumer(group -> {
            API.service.getUsers(group.getGroupid()).enqueue(new DefaultCallback<List<PublicUserData>>() {
                @Override
                public void onSucess(@Nullable List<PublicUserData> response) {
                    userSearchView.setQuery("", false);
                    groupId.set(group.getGroupid());

                    users.clear();

                    System.err.println("Added " + response);

                    for(PublicUserData userData : response) {
                        CombinedUser user = new CombinedUser(userData, 0);
                        user.setAdapter(userAdapter);
                        users.add(user);


                        onUserAdd(user);
                    }
                }
            });
        });


        var ctx = this;


        findViewById(R.id.btnFertig).setOnClickListener((v) -> {
            if(findViewById(R.id.betragChange).getVisibility() == View.VISIBLE) {
                return;
            }

            AtomicInteger i = new AtomicInteger(0);

            for (CombinedUser user : users) {
                API.service.createTRansaction(new TransactionCreateRequest(user.getUserData().getUserid(), user.getBalance(), groupId.get())).enqueue(new DefaultCallback<Transaction>() {
                    @Override
                    public void onSucess(@Nullable Transaction response) {
                        if(i.addAndGet(1) == users.size()) {
                            NavigationUtils.reloadSelf(ctx);
                        }
                    }
                });
            }
        });
    }

    private void onUserAdd(CombinedUser user) {
        if(user.getUserData().getUserid() == API.userID) {
            user.getUserData().setFirstname(this.getString(R.string.you));
            user.getUserData().setLastname("");
        }
        if(this.isSplitEven.get()) {
            onUserChange(user);
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

    @Override
    public Observable<Boolean> getIsSplitEven() {
        return this.isSplitEven;
    }

    @Override
    public Observable<Boolean> getDeleteMode() {
        return this.deleteMode;
    }

    public void onUserChange(CombinedUser user) {
        if(this.isSplitEven.get()) {
            double per_user = Math.ceil((totalSum.get() / users.size()) * 100) / 100;
            for(CombinedUser u : this.users) {
                u.setBalance(per_user);
            }
        }

        double sum = 0.0;
        for(CombinedUser u : this.users) {
            sum += u.getBalance();
        }

        this.userSum.set(sum);
    }

    @SuppressLint("SetTextI18n")
    private void checkSum() {
        TextView betragDifference = findViewById(R.id.betragChange);
        if(Math.abs(this.totalSum.get() - this.userSum.get()) < 0.01) {
            betragDifference.setVisibility(View.INVISIBLE);
        } else {
            betragDifference.setText(Math.floor((this.totalSum.get() - this.userSum.get()) * 100) / 100 + "");
            betragDifference.setVisibility(View.VISIBLE);
        }
    }
}
