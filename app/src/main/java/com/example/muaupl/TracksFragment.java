package com.example.muaupl;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TracksFragment extends Fragment {
    private ListView listViewTracks;

    ArrayAdapter<Track> tracksAdapter;
    ArrayList<Track> tracksList = new ArrayList<>();
    private int tabType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);

        Bundle args = getArguments();
        if (args != null) {
            tabType = args.getInt("tabType");
        }

        listViewTracks = view.findViewById(R.id.listViewTracks);
        retrieveTracks(tabType == 0); //параметр // в зависимости от tabType

        if (tracksList.isEmpty()) {
            Log.d("MyApp", "Массив треков пуст");
        } else {
              Log.d("MyApp", "Массив треков содержит треки");
        }

        // Изменил тип адаптера на ArrayAdapter<Track> и создал кастомный макет для каждого элемента списка tracks
        tracksAdapter = new TrackAdapter(requireActivity(), R.layout.item_track, tracksList);
        listViewTracks.setAdapter(tracksAdapter);

        listViewTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track selectedTrack = tracksList.get(position);
             //   Log.d("MyApp", "Selected track - Title: " + selectedTrack.getTitle() + ", Uri: " + getTrackUri(selectedTrack.getTitle()));

                // Добавляем логику для проверки пустоты массива при клике на трек
                if (tracksList.isEmpty()) {
                    Log.d("MyApp", "Массив треков пуст при клике на трек");
                    // Дополнительная логика, если массив пустой
                } else {
                    // Массив треков не пустой, продолжаем выполнение
                    Intent intent = new Intent(requireContext(), PlayerActivity.class);
                    intent.putExtra("trackTitle", selectedTrack.getTitle());
                    intent.putExtra("trackTime", selectedTrack.getTime());
                    String trackUri = getTrackUri(selectedTrack.getTitle());
                if (trackUri != null) {
                    intent.putExtra("trackUri", trackUri);

                    // Получаем правильный список треков перед передачей в PlayerActivity
                    ArrayList<Track> tracksList = getTracksList();

                    MusicPlayer musicPlayer = MusicPlayer.getInstance();
                    musicPlayer.stopPlayback();

                    ArrayList<Parcelable> parcelableTracksList = new ArrayList<>(tracksList);
                        Log.d("MyApp", "Проверка передачи массива треков: " + parcelableTracksList.toString());

                    intent.putParcelableArrayListExtra("trackList", parcelableTracksList);

                    startActivity(intent);
                    } else {
                        Log.e("MyApp", "URI имеет значение null для выбранной дорожки: " + selectedTrack.getTitle());
                    }
                }
            }
        });
        return view;
    }

    private String getTrackUri(String trackTitle) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DATA};
        String selection = MediaStore.Audio.Media.TITLE + "=?";
        String[] selectionArgs = {trackTitle};

        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            if (columnIndex >= 0) {
                String trackUri = cursor.getString(columnIndex);
                cursor.close();
                return trackUri;
            } else {
                Log.e("MyApp", "Не удалось получить индекс столбца для DATA.");
            }
        } else {
            Log.e("MyApp", "Курсор имеет значение null или пуст");
        }
        return null;
    }

    private void retrieveTracks(boolean isMusicFragment) {
        tracksList.clear(); // Очистить список перед добавлением новых треков
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.DURATION};

        // Условие для выбора строчек из музыкального фрагмента или диктофона
        String selection;
        if (isMusicFragment) {
            selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"; // Для фрагмента с музыкой
        } else {
            selection = MediaStore.Audio.Media.IS_MUSIC + "=0"; // Для фрагмента с диктофоном
        }

        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, selection, null, null);

        if (cursor != null) {
            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
            int durationIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);

            while (cursor.moveToNext()) {
                if (titleIndex != -1 && durationIndex != -1) {
                    String trackTitle = cursor.getString(titleIndex);
                    long trackDuration = cursor.getLong(durationIndex);

                    String trackTime = formatDuration(trackDuration);
                    String trackUri = getTrackUri(trackTitle);

                    if (trackUri != null) {
                        tracksList.add(new Track(trackTitle, trackTime, trackUri));
                    //    Log.d("MyApp", "Added track - Title: " + trackTitle + ", Uri: " + trackUri);
                    } else {
                        Log.e("MyApp", "trackUri имеет значение null для трека: " + trackTitle);
                    }
                }
            }
            cursor.close();
        }
    }
    public ArrayList<Track> getTracksList() {
        return tracksList;
    }
    private String formatDuration(long duration) {
        long minutes = (duration / 1000) / 60;
        long seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void filterTracks(String query) {
        ((TrackAdapter) listViewTracks.getAdapter()).getFilter().filter(query);
    }
    public void showAllTracks() {
        tracksList.clear();
        retrieveTracks(tabType == 0); // Передать параметр в зависимости от tabType

        if (tracksAdapter != null) {
            ((TrackAdapter) tracksAdapter).resetFilter();
        }
    }
}