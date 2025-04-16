package at.htlsaalfelden.UNSERsplit.fcm;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
import java.util.Map;
import java.util.Objects;

import at.htlsaalfelden.UNSERsplit.R;

public class FCMService extends FirebaseMessagingService {

    private static final String CHANNEL = "UNSERSPLIT";

    private void showNotification(String title, String content) {
        showNotification(title, content, this);
    }
    public static void showNotification(String title, String content, Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            //throw new RuntimeException("User did not grant permissions");
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.euro);

        Notification notification = builder.build();

        NotificationManagerCompat compat = NotificationManagerCompat.from(context);

        NotificationChannelCompat channelCompat = new NotificationChannelCompat.Builder(CHANNEL, IMPORTANCE_HIGH).setDescription(CHANNEL).setName(CHANNEL).build();

        compat.createNotificationChannel(channelCompat);

        compat.notify(0, notification);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> extra = message.getData();

        showNotification(extra.getOrDefault("title","No Title"), extra.getOrDefault("text", "No Text"));
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        System.out.println(token);
    }
}
