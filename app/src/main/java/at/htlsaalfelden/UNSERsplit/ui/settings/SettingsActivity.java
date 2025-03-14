package at.htlsaalfelden.UNSERsplit.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Space;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import at.htlsaalfelden.UNSERsplit.R;
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


        //Dynamic Buttons
        Button btnZurueck = findViewById(R.id.btnZurück);
        params = btnZurueck.getLayoutParams();
        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height =  (int)(displayMetrics.heightPixels * 0.065);
        width = (int) (displayMetrics.widthPixels * 0.35);

        params.height = height;
        params.width = width;
        btnZurueck.setLayoutParams(params);


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


        //Set all Editfields to "not editable"
        EditText d = findViewById(R.id.txtViewSettingVornameData);
        d.setInputType(InputType.TYPE_NULL);

        d = findViewById(R.id.txtViewSettingNameData);
        d.setInputType(InputType.TYPE_NULL);

        d = findViewById(R.id.txtViewSettingIbanData);
        d.setInputType(InputType.TYPE_NULL);

        d = findViewById(R.id.txtInputPasswortData);
        d.setInputType(InputType.TYPE_NULL);



        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent);
        });

        findViewById(R.id.btnZurück).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent);
        });

        findViewById(R.id.btnSpeichern).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent);
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
