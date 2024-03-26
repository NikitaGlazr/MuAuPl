package com.example.muaupl;

import static com.example.muaupl.PlayerActivity.ACTION_NEXT;
import static com.example.muaupl.PlayerActivity.ACTION_PLAY_PAUSE;
import static com.example.muaupl.PlayerActivity.ACTION_PREVIOUS;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.RemoteViews;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotificationHelper {
    private static final int NOTIFICATION_ID = 1;

    private NotificationManagerCompat notificationManager;

    public NotificationHelper(Context context) {
        notificationManager = NotificationManagerCompat.from(context);
    }

    private static final String CHANNEL_ID = "MusicPlayerChannel"; // объявление CHANNEL_ID
    private static final int MY_NOTIFICATION_PERMISSION_REQUEST = 123; // Уникальный id для запроса разрешения


    public void showNotification(Context context, String trackName, boolean isPlaying) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_big_layout);
        remoteViews.setTextViewText(R.id.tvAudioNameBigNotif, trackName);

        // Установка действий для кнопок Play/Pause
        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.btnPlayPauseBigNotif, R.drawable.arg_pause);
        } else {
            remoteViews.setImageViewResource(R.id.btnPlayPauseBigNotif, R.drawable.arg_play);
        }

        // Установка обработчиков нажатий на кнопки в уведомлении
        remoteViews.setOnClickPendingIntent(R.id.btnPrevBigNotif, getPendingIntent(context, ACTION_PREVIOUS));
        remoteViews.setOnClickPendingIntent(R.id.btnPlayPauseBigNotif, getPendingIntent(context, ACTION_PLAY_PAUSE));
        remoteViews.setOnClickPendingIntent(R.id.btnNextBigNotif, getPendingIntent(context, ACTION_NEXT));

        // Создание уведомления с кастомным макетом
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.music)
                .setCustomBigContentView(remoteViews)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        // Показ уведомления
        notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, MY_NOTIFICATION_PERMISSION_REQUEST);
        }
    }

    private PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, NotificationActionService.class);
        intent.setAction(action);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public void cancelNotification() {
        // Отмена уведомления
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
