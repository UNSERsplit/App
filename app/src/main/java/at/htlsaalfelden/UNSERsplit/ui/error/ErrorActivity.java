package at.htlsaalfelden.UNSERsplit.ui.error;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import at.htlsaalfelden.UNSERsplit.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_error);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();

        TextView errorCode = findViewById(R.id.txtErrorCode);
        TextView errorDetail = findViewById(R.id.txtErrorDetail);

        errorCode.setText(extras.getString("code"));
        errorDetail.setText(extras.getString("detail"));
    }

    @SuppressLint("PrivateApi")
    private static Application getApplicationUsingReflection() throws Exception {
        return (Application) Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication").invoke(null, (Object[]) null);
    }

    public static void showError(String errorCode, String errorDetail) {
        Application application = null;
        try {
            application = getApplicationUsingReflection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Intent intent = new Intent(application, ErrorActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("code", errorCode);
        intent.putExtra("detail", errorDetail);

        application.startActivity(intent);
    }

    public static void showError(int errorCode, String errorDetail) {
        showError("E:" + errorCode, errorDetail);
    }


}