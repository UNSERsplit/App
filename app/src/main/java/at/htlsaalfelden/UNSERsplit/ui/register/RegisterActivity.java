package at.htlsaalfelden.UNSERsplit.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import at.htlsaalfelden.UNSERsplit.MainActivity;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

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

        findViewById(R.id.btnRegistrieren).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        });

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}
