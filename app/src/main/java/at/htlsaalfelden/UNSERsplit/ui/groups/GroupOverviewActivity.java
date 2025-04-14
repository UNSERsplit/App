package at.htlsaalfelden.UNSERsplit.ui.groups;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.ui.NavigationUtils;
import at.htlsaalfelden.UNSERsplit.ui.register.RegisterActivity;

public class GroupOverviewActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_overview);

        NavigationUtils.initNavbar(this );

        var ctx = this;

        //dynamic ContentContainer
        ConstraintLayout layout = findViewById(R.id.contentContainer);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height =  (int)(displayMetrics.heightPixels * 0.62);
        int width = (int) (displayMetrics.widthPixels * 0.80);

        params.height = height;
        params.width = width;
        layout.setLayoutParams(params);

        //Dynamic Contentlayout
        ConstraintLayout gruppenSettingsContainer = findViewById(R.id.gruppenSettingsContainer);
        ViewGroup.LayoutParams params2 = gruppenSettingsContainer.getLayoutParams();
        DisplayMetrics displayMetrics2 = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics2);
        height =  (int)(displayMetrics2.heightPixels * 0.57);

        params2.height = height;

        gruppenSettingsContainer.setLayoutParams(params2);

        //Dynamic mitgliederContainer
        ConstraintLayout mitgliederContainer = findViewById(R.id.mitgliederContainer);
        ViewGroup.LayoutParams params3 = gruppenSettingsContainer.getLayoutParams();
        DisplayMetrics displayMetrics3 = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics3);
        height =  (int)(displayMetrics3.heightPixels * 0.57);

        params2.height = height;

        mitgliederContainer.setLayoutParams(params2);


        /*findViewById(R.id.mitgliederContainer).setOnClickListener(v -> {
                mitgliederContainer.setVisibility(View.VISIBLE);
                gruppenSettingsContainer.setVisibility(View.INVISIBLE);
        });

        findViewById(R.id.gruppenSettingsContainer).setOnClickListener(v -> {
            mitgliederContainer.setVisibility(View.INVISIBLE);
            gruppenSettingsContainer.setVisibility(View.VISIBLE);
        });*/

}}
