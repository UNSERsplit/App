package at.htlsaalfelden.UNSERsplit.ui.login;

import android.annotation.SuppressLint;
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
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.FailableCallback;
import at.htlsaalfelden.UNSERsplit.api.model.LoginRequest;
import at.htlsaalfelden.UNSERsplit.api.model.LoginResponse;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;
import okhttp3.Request;
import at.htlsaalfelden.UNSERsplit.ui.register.RegisterActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Invocation;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

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

        findViewById(R.id.btnregister).setOnClickListener(v -> {
            Intent myIntent = new Intent(this, RegisterActivity.class);
            startActivity(myIntent);
        });



        findViewById(R.id.btnlogin).setOnClickListener(v -> {
            LoginRequest request = new LoginRequest(
                    ((EditText) findViewById(R.id.txtVname)).getText().toString(),
                    ((EditText) findViewById(R.id.txtVpassword)).getText().toString()
            );

            API.service.login(
                    request).enqueue(
                    new FailableCallback<LoginResponse, LoginRequest>() {
                        @Override
                        public void onError(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response, LoginRequest requestData) {
                            if(response.code() == 401) {
                                ((EditText) findViewById(R.id.txtVname)).setError("Username/password wrong");
                                ((EditText) findViewById(R.id.txtVpassword)).setError("Username/password wrong");
                            }
                        }

                        @Override
                        public void onSucess(@Nullable LoginResponse response) {
                            if(response != null ){
                                API.token = response.getToken();

                                Intent myIntent = new Intent(ctx, HomeActivity.class);
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