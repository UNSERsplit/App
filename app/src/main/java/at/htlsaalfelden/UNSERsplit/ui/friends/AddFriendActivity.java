package at.htlsaalfelden.UNSERsplit.ui.friends;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.ui.NavigationUtils;


public class AddFriendActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_friend);
        NavigationUtils.initNavbar(this);

        var ctx = this;


        TextView txtViewAddFriend = findViewById(R.id.txtViewAddFriend);
        TextView textViewAnfrage = findViewById(R.id.textViewAnfrage);
        dynamicSize(txtViewAddFriend, 0.2,0.3);
        dynamicSize(textViewAnfrage, 0.2,0.3);



    }

    public void dynamicSize(View x, double heighta, double widtha){
        ViewGroup.LayoutParams params1 = x.getLayoutParams();
        DisplayMetrics displayMetrics1 = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics1);
        int height =  (int)(displayMetrics1.heightPixels * heighta);
        int width = (int) (displayMetrics1.widthPixels * widtha);

        params1.height = height;
        params1.width = width;
        x.setLayoutParams(params1);
    }
}
