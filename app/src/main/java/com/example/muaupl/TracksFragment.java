package com.example.muaupl;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TracksFragment extends Fragment {
    private ListView listViewTracks;

    // Изменил массив строк на массив объектов Track для хранения и отображения имени и времени трека
    private ArrayAdapter<Track> tracksAdapter;
    private ArrayList<Track> tracksList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);

        listViewTracks = view.findViewById(R.id.listViewTracks);
        retrieveTracks();

        // Изменил тип адаптера на ArrayAdapter<Track> и создал кастомный макет для каждого элемента списка tracks
        tracksAdapter = new TrackAdapter(requireActivity(), R.layout.item_track, tracksList);
        listViewTracks.setAdapter(tracksAdapter);

        return view;
    }

    private void retrieveTracks() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.DURATION};

        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
            int durationIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);

            while (cursor.moveToNext()) {
                if (titleIndex != -1 && durationIndex != -1) {
                    String trackTitle = cursor.getString(titleIndex);
                    long trackDuration = cursor.getLong(durationIndex);

                    String trackTime = formatDuration(trackDuration);

                    tracksList.add(new Track(trackTitle, trackTime));
                }
            }
            cursor.close();
        }
    }

    private String formatDuration(long duration) {
        long minutes = (duration / 1000) / 60;
        long seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}