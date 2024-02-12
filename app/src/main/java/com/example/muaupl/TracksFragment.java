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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TracksFragment extends Fragment {
    private ListView listViewTracks;
    private ArrayAdapter<String> tracksAdapter;
    private ArrayList<String> tracksList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);

        listViewTracks = view.findViewById(R.id.listViewTracks);
        retrieveTracks();

        tracksAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, tracksList);
        listViewTracks.setAdapter(tracksAdapter);

        return view;
    } private void retrieveTracks() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.TITLE};

        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String trackTitle = cursor.getString(0);
                tracksList.add(trackTitle);
            }
            cursor.close();
        }
    }
}