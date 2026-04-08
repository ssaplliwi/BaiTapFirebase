package com.example.baitapfirebase.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.baitapfirebase.MainActivity;
import com.example.baitapfirebase.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String CHANNEL_ID = "movie_reminder_channel";
    public static final String CHANNEL_NAME = "Nhắc giờ chiếu phim";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Gửi token lên Firebase Firestore để server có thể gửi notification
        sendTokenToServer(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Tạo notification channel cho Android 8.0+
        createNotificationChannel();

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String movieTitle = remoteMessage.getData().get("movieTitle");
        String showTime = remoteMessage.getData().get("showTime");

        // Nếu không có title/body từ notification, dùng data
        if (title == null) title = "Nhắc giờ chiếu phim";
        if (body == null) body = "Bạn có lịch xem phim sắp tới!";

        if (movieTitle != null && showTime != null) {
            body = "Phim: " + movieTitle + "\nGiờ chiếu: " + showTime;
        }

        showNotification(title, body);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Thông báo nhắc giờ chiếu phim");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void showNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    private void sendTokenToServer(String token) {
        // Gửi FCM token lên Firestore để server gửi notification
        com.google.firebase.firestore.FirebaseFirestore db =
                com.google.firebase.firestore.FirebaseFirestore.getInstance();

        String userId = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null
                ? com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "anonymous";

        java.util.Map<String, Object> tokenData = new java.util.HashMap<>();
        tokenData.put("token", token);
        tokenData.put("userId", userId);
        tokenData.put("timestamp", java.util.Calendar.getInstance().getTime().toString());

        db.collection("fcm_tokens").document(userId)
                .set(tokenData)
                .addOnCompleteListener(task -> {
                    android.util.Log.d("FCM", "Token saved: " + task.isSuccessful());
                });
    }
}
