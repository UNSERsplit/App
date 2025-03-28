package at.htlsaalfelden.UNSERsplit.ui.transaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;
import at.htlsaalfelden.UNSERsplit.ui.settings.SettingsActivity;


public class TransactionActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);

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

        }

}
