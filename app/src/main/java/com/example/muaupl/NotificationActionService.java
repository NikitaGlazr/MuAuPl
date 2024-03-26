package com.example.muaupl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificationActionService extends Service {
    private static final String ACTION_PREVIOUS = "com.example.muaupl.action.PREVIOUS";
    private static final String ACTION_PLAY_PAUSE = "com.example.muaupl.action.PLAY_PAUSE";
    private static final String ACTION_NEXT = "com.example.muaupl.action.NEXT";
    private MediaPlayerManager mediaPlayerManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_PREVIOUS:
                    playPreviousTrack();
                    break;
                case ACTION_PLAY_PAUSE:
                    togglePlayPause();
                    break;
                case ACTION_NEXT:
                    playNextTrack();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void playPreviousTrack() {
        if (mediaPlayerManager != null) {
            mediaPlayerManager.playPreviousTrack();
        }
    }

    private void togglePlayPause() {
        if (mediaPlayerManager != null) {
            mediaPlayerManager.togglePlayPause();
        }
    }

    private void playNextTrack() {
        if (mediaPlayerManager != null) {
            mediaPlayerManager.playNextTrack();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayerManager = new MediaPlayerManager();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}