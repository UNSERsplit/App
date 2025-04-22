package at.htlsaalfelden.UNSERsplit.fcm;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import at.htlsaalfelden.UNSERsplit.MainActivity;
import at.htlsaalfelden.UNSERsplit.NoLib.ReflectionUtils;
import at.htlsaalfelden.UNSERsplit.R;
import at.htlsaalfelden.UNSERsplit.api.API;
import at.htlsaalfelden.UNSERsplit.ui.groups.GroupOverviewActivity;

public class FCMService extends FirebaseMessagingService {

    private static final String CHANNEL = "UNSERSPLIT";

    private void showNotification(Map<String, String> extra, int id) {
        showNotification(extra, id,this);
    }
    public static void showNotification(Map<String, String> extra, int id, Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            //throw new RuntimeException("User did not grant permissions");
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL);
        builder.setContentTitle(extra.getOrDefault("title","No Title"));
        builder.setContentText(extra.getOrDefault("text", "No Text"));

        addExtra(builder, extra, context);

        builder.setSmallIcon(R.drawable.euro);

        Notification notification = builder.build();

        NotificationManagerCompat compat = NotificationManagerCompat.from(context);

        NotificationChannelCompat channelCompat = new NotificationChannelCompat.Builder(CHANNEL, IMPORTANCE_HIGH).setDescription(CHANNEL).setName(CHANNEL).build();

        compat.createNotificationChannel(channelCompat);

        compat.notify(id, notification);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> extra = message.getData();

        showNotification(extra, ReflectionUtils.random.nextInt());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        API.service.setDeviceToken(token);
    }

    private static void addExtra(NotificationCompat.Builder builder, Map<String, String> extra, Context context) {
        String action = extra.getOrDefault("action", "");

        if(Objects.equals(action, "showGroup")) {
            String rawGroupId = extra.getOrDefault("groupId", "-1");
            Integer groupId = Integer.parseInt(rawGroupId);

            Intent myIntent = new Intent(context, GroupOverviewActivity.class);
            myIntent.putExtra("GROUP", groupId);

            addIntent(builder, myIntent, context);
        }
    }

    private static void addIntent(NotificationCompat.Builder builder, Intent innerIntent, Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("redirect", innerIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
    }
}
