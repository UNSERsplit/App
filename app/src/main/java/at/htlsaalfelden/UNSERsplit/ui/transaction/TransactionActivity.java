package at.htlsaalfelden.UNSERsplit.ui.transaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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

        }

}
