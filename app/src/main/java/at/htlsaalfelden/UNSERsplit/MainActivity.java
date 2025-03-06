package at.htlsaalfelden.UNSERsplit;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.FailableCallback;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;
import at.htlsaalfelden.UNSERsplit.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

        var ctx = this;

        API.service.test().enqueue(new FailableCallback<>() {
            @Override
            public void on400(@NonNull Call<String> call, @NonNull Response<String> response, Object requestData) {
                if(response.code() == 401) {
                    Intent myIntent = new Intent(ctx, LoginActivity.class);
                    startActivity(myIntent);
                }
            }

            @Override
            public void onSucess(@Nullable String response) {
                Intent myIntent = new Intent(ctx, HomeActivity.class);
                startActivity(myIntent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}