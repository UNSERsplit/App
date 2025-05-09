package at.htlsaalfelden.UNSERsplit.ui;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.ui.home.HomeActivity;
import at.htlsaalfelden.UNSERsplit.ui.settings.SettingsActivity;
import at.htlsaalfelden.UNSERsplit.ui.transaction.TransactionActivity;

public abstract class NavigationUtils {
    private NavigationUtils() {}

    private static final Class<?>[] navigationTargets = new Class[] {
            HomeActivity.class,
            TransactionActivity.class,
            SettingsActivity.class
    };

    public static void initNavbar(AppCompatActivity activity) {
        BottomNavigationView navigationView = activity.findViewById(R.id.bottomNavigationView);
        Menu menu = navigationView.getMenu();

        for (int i = 0; i < navigationTargets.length; i++) {
            MenuItem item = menu.getItem(i);

            int finalI = i;
            activity.findViewById(item.getItemId()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, navigationTargets[finalI]);
                    activity.startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                }
            });

            if(activity.getClass().equals(navigationTargets[i])) { // immer das aktuelle Menü
                item.setChecked(true);
            }
        }

        initScreen(activity);
    }

    public static void initScreen(AppCompatActivity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static void reloadSelf(AppCompatActivity activity) {
        Bundle extras = activity.getIntent().getExtras();

        Intent myIntent = new Intent(activity, activity.getClass());
        myIntent.replaceExtras(extras);
        myIntent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(myIntent);
    }
}
