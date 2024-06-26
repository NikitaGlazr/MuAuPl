package com.example.muaupl;

import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;

public class MediaPlayerManager {
    private MediaPlayer mediaPlayer;
    private static MediaPlayerManager instance;
    private int  currentTrackIndex = 0;
    private ArrayList<Track> tracksList;
    public MediaPlayerManager() {
        this.mediaPlayer = new MediaPlayer();
        // Дополнительная настройка медиаплеера
    }
    public void setTracksList(ArrayList<Track> tracksList) {
        this.tracksList = tracksList;
    }
    public void playNextTrack() {
        // Логика для проигрывания следующего трека
        currentTrackIndex++; // Увеличиваем индекс текущего трека

        if (tracksList != null && currentTrackIndex < tracksList.size()) {
            Track nextTrack = tracksList.get(currentTrackIndex);
            if (nextTrack != null && nextTrack.getTrackUri() != null) {
                playTrack(nextTrack);
            } else {
                Log.e("MediaPlayerManager", "Неверный трек или trackUri для следующего трека имеет значение null.");
            }
        } else {
            Log.d("MediaPlayerManager", "Больше нет треков для воспроизведения");
            // Можно добавить логику для обработки ситуации, когда больше нет треков для воспроизведения
        }
    }

    private void playTrack(Track track) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(track.getTrackUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d("MediaPlayerManager", "Воспроизводимый трек: " + track.getTitle());
        } catch (Exception e) {
            Log.e("MediaPlayerManager", "Ошибка воспроизведения трека: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void playPreviousTrack() {
        // Логика для проигрывания предыдущего трека
        currentTrackIndex--; // Уменьшаем индекс текущего трека

        if (tracksList != null && currentTrackIndex >= 0) {
            Track previousTrack = tracksList.get(currentTrackIndex);
            if (previousTrack != null && previousTrack.getTrackUri() != null) {
                playTrack(previousTrack);
            } else {
                Log.e("MediaPlayerManager", "Неверный трек или trackUri имеет значение null для предыдущего трека.");
            }
        } else {
            Log.d("MediaPlayerManager", "Нет предыдущих треков для воспроизведения");
            // Можно добавить логику для обработки ситуации, когда больше нет предыдущих треков для воспроизведения
        }
    }

    public void togglePlayPause() {
        // Логика для переключения проигрывания трека
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    public void stopPlayback() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}
