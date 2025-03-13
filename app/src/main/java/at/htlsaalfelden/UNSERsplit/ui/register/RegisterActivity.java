package at.htlsaalfelden.UNSERsplit.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import at.htlsaalfelden.UNSERsplit.MainActivity;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.FailableCallback;
import at.htlsaalfelden.UNSERsplit.api.model.LoginRequest;
import at.htlsaalfelden.UNSERsplit.api.model.LoginResponse;
import at.htlsaalfelden.UNSERsplit.api.model.User;
import at.htlsaalfelden.UNSERsplit.api.model.UserCreateRequest;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;
import at.htlsaalfelden.UNSERsplit.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        var ctx = this;

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
            UserCreateRequest request = new UserCreateRequest(
                    ((EditText) findViewById(R.id.txtInputVorname)).getText().toString(),
                    ((EditText) findViewById(R.id.txtInputNachname)).getText().toString(),
                    ((EditText) findViewById(R.id.txtInputEmail)).getText().toString(),
                    ((EditText) findViewById(R.id.txtInputIban)).getText().toString(),
                    ((EditText) findViewById(R.id.txtInputPasswort)).getText().toString()
            );

            API.service.register(
                    request).enqueue(
                    new FailableCallback<User, UserCreateRequest>() {
                        @Override
                        public void onError(@NonNull Call<User> call, @NonNull Response<User> response, UserCreateRequest requestData) {

                        }

                        @Override
                        public void onSucess(@Nullable User response) {
                            if(response != null ){
                                Intent myIntent = new Intent(ctx, LoginActivity.class);
                                startActivity(myIntent);
                            }

                        }
                    }
            );
        });

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}
