package at.htlsaalfelden.UNSERsplit.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.model.User;
import at.htlsaalfelden.UNSERsplit.api.model.UserCreateRequest;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;

public class SettingsActivity extends AppCompatActivity {




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        ConstraintLayout layout = findViewById(R.id.contentLayout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height =  (int)(displayMetrics.heightPixels * 0.6);
        int width = (int) (displayMetrics.widthPixels * 0.75);

        params.height = height;
        params.width = width;
        layout.setLayoutParams(params);

        //Dynamic Vorname
        ConstraintLayout VornameInnerContainer = findViewById(R.id.VornameInnerContainer);
        params = VornameInnerContainer.getLayoutParams();
        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height =  (int)(displayMetrics.heightPixels * 0.1);
        width = (int) (displayMetrics.widthPixels * 0.6);

        params.height = height;
        params.width = width;
        VornameInnerContainer.setLayoutParams(params);

        //Dynamic Nachname
        ConstraintLayout NachnameInnerContainer = findViewById(R.id.NachnameInnerContainer);
        params = NachnameInnerContainer.getLayoutParams();
        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height =  (int)(displayMetrics.heightPixels * 0.1);
        width = (int) (displayMetrics.widthPixels * 0.6);

        params.height = height;
        params.width = width;
        NachnameInnerContainer.setLayoutParams(params);

        //Dynamic Iban
        ConstraintLayout IbanInnerContainer = findViewById(R.id.IbanInnerContainer);
        params = IbanInnerContainer.getLayoutParams();
        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height =  (int)(displayMetrics.heightPixels * 0.1);
        width = (int) (displayMetrics.widthPixels * 0.6);

        params.height = height;
        params.width = width;
        IbanInnerContainer.setLayoutParams(params);

        //Dynamic Passwort
        ConstraintLayout PasswortInnerContainer = findViewById(R.id.PasswortInnerContainer);
        params = PasswortInnerContainer.getLayoutParams();
        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height =  (int)(displayMetrics.heightPixels * 0.1);
        width = (int) (displayMetrics.widthPixels * 0.6);

        params.height = height;
        params.width = width;
        PasswortInnerContainer.setLayoutParams(params);

        //Set all Editfields to "not editable"
        EditText vorname = findViewById(R.id.txtViewSettingVornameData);
        vorname.setInputType(InputType.TYPE_NULL);

        EditText name = findViewById(R.id.txtViewSettingNameData);
        name.setInputType(InputType.TYPE_NULL);

        EditText iban = findViewById(R.id.txtViewSettingIbanData);
        iban.setInputType(InputType.TYPE_NULL);

        EditText passwort = findViewById(R.id.txtInputPasswortData);
        passwort.setInputType(InputType.TYPE_NULL);

        var ctx = this;

        final User[] currentUser = {null};

        /*API.service.getUser().enqueue(new DefaultCallback<User>() {
            @Override
            public void onSucess(@Nullable User response) {
                vorname.setText(response.getFirstname());
                name.setText(response.getLastname());
                iban.setText(response.getIban());
                passwort.setText("");

                currentUser[0] = response;
            }
        });*/



        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent);
        });

        findViewById(R.id.btnZurück).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent);
        });

        findViewById(R.id.btnSpeichern).setOnClickListener(v -> {
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

        findViewById(R.id.btnSettingVornameChange).setOnClickListener(v -> {
            EditText editText = findViewById(R.id.txtViewSettingVornameData);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        });

        findViewById(R.id.btnSettingVornameChange).setOnClickListener(v -> {
            EditText editText = findViewById(R.id.txtViewSettingNameData);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        });

        findViewById(R.id.btnSettingIbanChange).setOnClickListener(v -> {
            EditText editText = findViewById(R.id.txtViewSettingIbanData);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        });

        findViewById(R.id.btnSettingPasswortChange).setOnClickListener(v -> {
            EditText editText = findViewById(R.id.txtInputPasswortData);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        });

       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}
