package com.example.muaupl;

import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer {
    private static MusicPlayer instance;
    private MediaPlayer mediaPlayer;
    private ArrayList<Track> tracksList = new ArrayList<>();
    private int currentTrackIndex = 0;
    private boolean isPlaying;

    private MusicPlayer() {
        mediaPlayer = new MediaPlayer();
        isPlaying = false;
    }

    public static MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void stopPlayback() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            isPlaying = false;
        }
    }
    public void setTracks(ArrayList<Track> tracks) {
        this.tracksList = tracks;
    }
    public ArrayList<Track> getTracks() {
        return tracksList;
    }
    public void playNextTrack() {
        if (!tracksList.isEmpty()) {
            currentTrackIndex = (currentTrackIndex + 1) % tracksList.size();
            playTrack(currentTrackIndex);
        }
    }

    public void playPreviousTrack() {
        if (!tracksList.isEmpty()) {
            currentTrackIndex = (currentTrackIndex - 1 + tracksList.size()) % tracksList.size();
            playTrack(currentTrackIndex);
        }
    }

    public void togglePlayPause() {
        if (isPlaying) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
        isPlaying = !isPlaying;
    }

    private void playTrack(int index) {
        Track track = tracksList.get(index);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(track.getTrackUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
