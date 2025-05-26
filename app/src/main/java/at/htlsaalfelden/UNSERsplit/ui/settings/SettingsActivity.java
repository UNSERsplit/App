package at.htlsaalfelden.UNSERsplit.ui.settings;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Space;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import at.htlsaalfelden.UNSERsplit.MainActivity;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.User;
import at.htlsaalfelden.UNSERsplit.api.model.UserCreateRequest;
import at.htlsaalfelden.UNSERsplit.ui.IBANutils;
import at.htlsaalfelden.UNSERsplit.ui.NavigationUtils;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;

public class SettingsActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        NavigationUtils.initNavbar(this);


        ConstraintLayout layout = findViewById(R.id.contentLayout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height =  (int)(displayMetrics.heightPixels * 0.6);
        int width = (int) (displayMetrics.widthPixels * 0.79);

        params.height = height;
        params.width = width;
        layout.setLayoutParams(params);


        //Dynamic Buttons
        Button btnLoeschen = findViewById(R.id.btnLoeschen);
        params = btnLoeschen.getLayoutParams();
        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height =  (int)(displayMetrics.heightPixels * 0.065);
        width = (int) (displayMetrics.widthPixels * 0.35);

        params.height = height;
        params.width = width;
        btnLoeschen.setLayoutParams(params);


        //Dynamic Buttons
        Button btnSpeichern = findViewById(R.id.btnSpeichern);
        params = btnSpeichern.getLayoutParams();
        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height =  (int)(displayMetrics.heightPixels * 0.065);
        width = (int) (displayMetrics.widthPixels * 0.35);

        params.height = height;
        params.width = width;
        btnSpeichern.setLayoutParams(params);

        //Dynamic Space
        Space buttonSpace = findViewById(R.id.spaceBtn);
        params = buttonSpace.getLayoutParams();
        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = (int) (displayMetrics.widthPixels * 0.2);

        params.width = width;
        buttonSpace.setLayoutParams(params);


        var ctx = this;

        EditText vorname = findViewById(R.id.textViewSettingVornamData);
        EditText name = findViewById(R.id.txtViewSettingNamData);
        EditText iban = findViewById(R.id.txtViewSettingIbaData);
        EditText passwort = findViewById(R.id.txtViewSettingpassworData);

        iban.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty() && !IBANutils.isValidIban(s.toString())) {
                    iban.setError("Iban is invalid");
                } else {
                    iban.setError(null);
                }
            }
        });

        final User[] currentUser = {null};

        API.service.getUser().enqueue(new DefaultCallback<User>() {
            @Override
            public void onSucess(@Nullable User response) {
                vorname.setText(response.getFirstname());
                name.setText(response.getLastname());
                iban.setText(response.getIban());
                passwort.setHint("*****");

                currentUser[0] = response;
            }
        });

        findViewById(R.id.btnLoeschen).setOnClickListener(v -> {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
            materialAlertDialogBuilder.setMessage("Are you sure to delete your account. This action can not be undone!");
            materialAlertDialogBuilder.setTitle("Delete your account");

            materialAlertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
                API.service.deleteUser();
            });

            materialAlertDialogBuilder.setNegativeButton("No", (dialog, which) -> {

            });

            materialAlertDialogBuilder.create().show();

        });

        findViewById(R.id.btnSpeichern).setOnClickListener(v -> {
            if(!iban.getText().toString().isEmpty() && !IBANutils.isValidIban(iban.getText().toString())) {
                return;
            }

            API.service.updateUser(new UserCreateRequest(vorname.getText().toString(),
                                                        name.getText().toString(),
                                                        currentUser[0].getEmail(),
                                                        iban.getText().toString(),
                                                        passwort.getText().toString())).enqueue(new DefaultCallback<User>() {
                @Override
                public void onSucess(@Nullable User response) {
                    Intent myIntent = new Intent(ctx, HomeActivity.class);
                    startActivity(myIntent);
                }
            });
        });
    }
}
