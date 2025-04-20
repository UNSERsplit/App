package at.htlsaalfelden.UNSERsplit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.logging.Logger;

import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.api.DefaultCallback;
import at.htlsaalfelden.UNSERsplit.api.FailableCallback;
import at.htlsaalfelden.UNSERsplit.api.model.User;
import at.htlsaalfelden.UNSERsplit.ui.error.ErrorActivity;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;
import at.htlsaalfelden.UNSERsplit.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        afterCreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        overridePendingTransition(0,0);

        API.loadToken(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, 0);
        } else {
            afterCreate();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void afterCreate() {
        var ctx = this;

        API.service.getUser().enqueue(new FailableCallback<>() {
            @Override
            public void on400(@NonNull Call<User> call, @NonNull Response<User> response, Object requestData) {
                if(response.code() == 401) {
                    Intent myIntent = new Intent(ctx, LoginActivity.class);
                    myIntent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myIntent);
                }
            }

            @Override
            public void onSucess(@Nullable User response) {
                API.userID = response.getUserid();

                fcmInit();

                Intent myIntent = new Intent(ctx, HomeActivity.class);
                myIntent.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
            }
        });
    }

    public static void fcmInit() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()) {
                    System.err.println(task.getException());
                    return;
                }

                String token = task.getResult();

                API.service.setDeviceToken(token).enqueue(new DefaultCallback<User>() {
                    @Override
                    public void onSucess(@Nullable User response) {

                    }
                });
            }
        });
    }
}