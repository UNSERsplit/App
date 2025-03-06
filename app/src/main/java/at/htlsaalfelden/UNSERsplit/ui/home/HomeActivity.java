package at.htlsaalfelden.UNSERsplit.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import at.htlsaalfelden.UNSERsplit.MainActivity;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.ui.settings.SettingsActivity;

public class HomeActivity extends AppCompatActivity {


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        Fade fade = new Fade();
        fade.excludeTarget(R.id.bottomNavigationView, true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        getWindow().setReenterTransition(fade);
        getWindow().setReturnTransition(fade);
        getWindow().setSharedElementEnterTransition(null);
        getWindow().setSharedElementExitTransition(null);
        getWindow().setSharedElementReenterTransition(null);
        getWindow().setSharedElementReturnTransition(null);

        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, HomeActivity.class);
            startActivity(myIntent);
        });

        findViewById(R.id.navSettings).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
        });

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}