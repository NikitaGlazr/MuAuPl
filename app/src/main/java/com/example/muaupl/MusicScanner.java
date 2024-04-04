package com.example.muaupl;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import java.util.concurrent.TimeUnit;

public class MusicScanner {
    public ArrayList<Track> scanMusicFiles(String folderPath) throws IOException {
        ArrayList<Track> tracksList = new ArrayList<>();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isMediaFile(file.getName())) {
                        String title = file.getName();
                        String time = getTrackDuration(file.getAbsolutePath(), retriever);
                        String uri = file.getAbsolutePath();
                        Track track = new Track(title, time, uri);
                        tracksList.add(track);
                    }
                }
            }
        }
        retriever.release();
        return tracksList;
    }

    private boolean isMediaFile(String fileName) {
        String[] supportedExtensions = {".mp3", ".wav", ".ogg"};
        for (String ext : supportedExtensions) {
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
    private String getTrackDuration(String filePath, MediaMetadataRetriever retriever) {
        try {
            retriever.setDataSource(filePath);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long durationInMillis = Long.parseLong(duration);

            return String.format(Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(durationInMillis),
                    TimeUnit.MILLISECONDS.toSeconds(durationInMillis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationInMillis)));
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
