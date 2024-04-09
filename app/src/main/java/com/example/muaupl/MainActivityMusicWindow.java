package com.example.muaupl;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivityMusicWindow extends AppCompatActivity{
    private Toolbar mToolbar;
    private boolean isSearchBarOpened = false;
    MenuItem searchItem;
    MenuItem item;
    EditText editTextSearch;
    private ViewPager viewPager;
    public ArrayList<Track> tracksList = new ArrayList<>();
    public ArrayAdapter<Track> tracksAdapter;private ImageView playPauseImageView;
    private LinearLayout playerControlsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_music_window);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mToolbar.setLogo(R.drawable.icomusic);
        viewPager = findViewById(R.id.viewPager);

        ImageView prevTrackImageView = findViewById(R.id.prevTrackImageView);
        playPauseImageView = findViewById(R.id.playPauseImageView);
        ImageView nextTrackImageView = findViewById(R.id.nextTrackImageView);

        prevTrackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия на кнопку "Предыдущий трек"
                playPreviousTrack();
            }
        });

        playPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия на кнопку "Воспроизведение/Пауза"
                togglePlayPause();
            }
        });

        nextTrackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия на кнопку "Следующий трек"
                playNextTrack();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        searchItem = menu.findItem(R.id.action_search_input);
        item = menu.findItem(R.id.action_search);
        editTextSearch = searchItem.getActionView().findViewById(R.id.edit_text_search);
        editTextSearch.setVisibility(View.INVISIBLE);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TracksFragment(), "Музыка", 0); // 0 для музыки
        viewPagerAdapter.addFragment(new TracksFragment(), "Диктофон", 1); // 1 для диктофона
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        item.setOnMenuItemClickListener(menuItem -> {
            if (!isSearchBarOpened) {
                isSearchBarOpened = true;
                editTextSearch.setVisibility(View.VISIBLE);

                editTextSearch.setVisibility(View.VISIBLE);
                editTextSearch.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);

                item.setIcon(R.drawable.iconsclose);
            } else {
                isSearchBarOpened = false;
                editTextSearch.setText("");
                editTextSearch.setVisibility(View.INVISIBLE);
                item.setIcon(R.drawable.iconsearch);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
            }
            return true;
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    item.setIcon(R.drawable.iconsclose);
                } else {
                    item.setIcon(R.drawable.iconsearch);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Drawable searchIcon = item.getIcon();
        if (searchIcon != null) {
            searchIcon.mutate();
            searchIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            item.setIcon(searchIcon);
        }

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String searchText = editTextSearch.getText().toString();
                performSearch(searchText);
                editTextSearch.setVisibility(View.INVISIBLE);
                editTextSearch.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSearch.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            if (!isSearchBarOpened) {
                isSearchBarOpened = true;
                editTextSearch.requestFocus();
                editTextSearch.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
            } else {
                if (editTextSearch.getVisibility() == View.VISIBLE) {
                    editTextSearch.requestFocus();
                } else {
                    editTextSearch.setVisibility(View.VISIBLE);
                    editTextSearch.requestFocus();
                }
            }
            return true;
        } else if (id == R.id.setting) {
            Intent intent = new Intent(MainActivityMusicWindow.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_clear_search) {
            editTextSearch.setText("");
            performSearch("");
            return true;
        } else if (id == R.id.action_settings) {
            // Обработка меню настроек
            return true;
        } else if (id == R.id.sort_by_name) {
            // Сортировка по имени
            sortTracksByName();
            return true;
        } else if (id == R.id.sort_by_file_time) {
            // Сортировка по времени файла
            sortTracksByFileTime();
            return true;
        } else if (id == R.id.sort_by_type) {
            // Сортировка по расширению
            sortTracksByType();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performSearch(String query) {
        TracksFragment tracksFragment = (TracksFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
        if (tracksFragment != null) {
            if (query.isEmpty()) {
                // Сбросить поиск
                tracksFragment.showAllTracks();
            } else {
                // Выполнить поиск
                tracksFragment.filterTracks(query);
            }
        }
    }

    private void sortTracksByName() {
        TracksFragment tracksFragment = (TracksFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
        if (tracksFragment != null) {
            Collections.sort(tracksFragment.tracksList, new Comparator<Track>() {
                @Override
                public int compare(Track track1, Track track2) {
                    return track1.getTitle().compareTo(track2.getTitle());
                }
            });
            tracksFragment.tracksAdapter.notifyDataSetChanged();
        }
    }

    private void sortTracksByFileTime() {
        TracksFragment tracksFragment = (TracksFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
        if (tracksFragment != null) {
            Collections.sort(tracksFragment.tracksList, new Comparator<Track>() {
                @Override
                public int compare(Track track1, Track track2) {
                    // Сравниваем по длине воспроизведения песни
                    return Long.compare(track2.getDuration(), track1.getDuration());
                }
            });
            tracksFragment.tracksAdapter.notifyDataSetChanged();
        }
    }

    private void sortTracksByType() {
        TracksFragment tracksFragment = (TracksFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + viewPager.getCurrentItem());
        if (tracksFragment != null) {
            Collections.sort(tracksFragment.tracksList, new Comparator<Track>() {
                @Override
                public int compare(Track track1, Track track2) {
                    String extension1 = getFileExtension(track1.getTitle());
                    String extension2 = getFileExtension(track2.getTitle());
                    return extension1.compareTo(extension2);
                }
                public String getFileExtension(String filename) {
                    int dotPos = filename.lastIndexOf(".");
                    if (dotPos == -1) {
                        return "";
                    }
                    return filename.substring(dotPos + 1);
                }
            });
            tracksFragment.tracksAdapter.notifyDataSetChanged();
        }
    }

    private void playNextTrack() {
        MusicPlayer.getInstance().playNextTrack();
    }

    private void playPreviousTrack() {
        MusicPlayer.getInstance().playPreviousTrack();
    }

    private void togglePlayPause() {
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (musicPlayer.isPlaying()) {
            musicPlayer.togglePlayPause();
            playPauseImageView.setImageResource(R.drawable.arg_play_white);
        } else {
            musicPlayer.togglePlayPause();
            playPauseImageView.setImageResource(R.drawable.arg_pause_white);
        }
    }
}
