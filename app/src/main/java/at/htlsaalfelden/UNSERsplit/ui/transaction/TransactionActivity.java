package at.htlsaalfelden.UNSERsplit.ui.transaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
            ConstraintLayout a = findViewById(R.id.costumSlpitContainer);
            a.setVisibility(View.GONE);
            a = findViewById(R.id.halfSlpitContainer);
            a.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.textViewbtnCostum).setOnClickListener(v -> {
            ConstraintLayout a = findViewById(R.id.halfSlpitContainer);
            a.setVisibility(View.GONE);
            a = findViewById(R.id.costumSlpitContainer);
            a.setVisibility(View.VISIBLE);
        });

        }

}
