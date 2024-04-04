package com.example.muaupl;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    private TextView trackTitleTextView;
    private TextView trackTimeTextView;
    private TextView trackUriTextView;
    private boolean isPlaying = false;
    private ImageView ivPlayPause;
    private GestureDetector gestureDetector;
    private SeekBar seekBar;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private MediaPlayer mediaPlayer;

    private int currentTrackIndex;
    private TextView tvSongTitle;
    public static final String ACTION_PREVIOUS = "com.example.muaupl.action.PREVIOUS";
    public static final String ACTION_PLAY_PAUSE = "com.example.muaupl.action.PLAY_PAUSE";
    public static final String ACTION_NEXT = "com.example.muaupl.action.NEXT";
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();

        notificationHelper = new NotificationHelper(this);

        seekBar = findViewById(R.id.seekBar);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);

        trackTitleTextView = findViewById(R.id.tvSongTitle);
        trackTimeTextView = findViewById(R.id.tvTotalTime);

        ImageView ivPrevious = findViewById(R.id.ivPrevious);
        ImageView ivNext = findViewById(R.id.ivNext);

        String trackUri = getIntent().getStringExtra("trackUri");
        Log.d("PlayerActivity", "Полученный trackUri: " + trackUri);


        mediaPlayer = MusicPlayer.getInstance().getMediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // При завершении проигрывания трека можно сбросить SeekBar и текущее время
                seekBar.setProgress(0);
                tvCurrentTime.setText("00:00");
                playNextTrack();
            }
        });

        //для обработки свайпов на экране.
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getY() - e1.getY() > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });

        if (getIntent().getBooleanExtra("STOP_PLAYBACK", false)) {
            MusicPlayer musicPlayer = MusicPlayer.getInstance();
            musicPlayer.stopPlayback();
        }
        else
        {
            mediaPlayer = MusicPlayer.getInstance().getMediaPlayer();

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(trackUri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Log.d("PlayerActivity", "Трек воспроизводится успешно с URI: " + trackUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ivPlayPause = findViewById(R.id.ivPlayPause);
            ivPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            ivPlayPause.setImageResource(R.drawable.arg_play);
                        } else {
                            mediaPlayer.start();
                            ivPlayPause.setImageResource(R.drawable.arg_pause);
                        }
                        isPlaying = !isPlaying;
                    }
                }
            });

            MusicScanner musicScanner = new MusicScanner();
            ArrayList<Track> tracksList = null;
            try {
                tracksList = musicScanner.scanMusicFiles("/storage/emulated/0/Download/");
                MusicPlayer.getInstance().setTracks(tracksList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Log.d("PlayerActivity", "Содержимое массива треков: " + tracksList.toString());

            if (!tracksList.isEmpty()) {
                for (Track track : tracksList) {
                    if (track != null) {
                        if (track.getTrackUri() != null && !track.getTrackUri().isEmpty()) {
                            Log.d("PlayerActivity", "Отслеживать Uri для " + track.getTitle() + ": " + track.getTrackUri());
                        } else {
                            Log.e("PlayerActivity", "Неверный трек или пустой trackUri для трека:" + track.getTitle());
                        }
                    } else {
                        Log.e("PlayerActivity", "Неверный объект отслеживания");
                    }
                }
            }


            Log.d("PlayerActivity", "Содержимое массива треков: " + tracksList.toString());
            if (intent != null) {
                String trackTitle = intent.getStringExtra("trackTitle");
                String trackTime = intent.getStringExtra("trackTime");
                trackUri = intent.getStringExtra("trackUri");

                tvSongTitle = findViewById(R.id.tvSongTitle);
                tvSongTitle.setText(trackTitle);

                tvTotalTime = findViewById(R.id.tvTotalTime);
                tvTotalTime.setText(trackTime);

                currentTrackIndex = 0;

                if (trackUri != null) {
                    Log.d("PlayerActivity", "trackUri value: " + trackUri);

                    //код для проверки выполнения после получения trackUri
                    Log.d("PlayerActivity", "Полученный trackUri: " + trackUri);

                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(trackUri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }if (trackTitle != null && trackTime != null && trackUri != null) {
                        Log.d("PlayerActivity", "Track Title: " + trackTitle);
                        Log.d("PlayerActivity", "Track Time: " + trackTime);
                        Log.d("PlayerActivity", "Track Uri: " + trackUri);
                    } else {
                        Log.e("PlayerActivity", "Одно или несколько полей данных имеют значение null");
                    }
                } else {
                    Log.e("PlayerActivity", "trackUri равен нулю");
                }
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Thread.sleep(1000); // Обновление каждую секунду
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayer.isPlaying()) {
                                    int currentPosition = mediaPlayer.getCurrentPosition();
                                    int totalDuration = mediaPlayer.getDuration();

                                    // Обновление текущего времени в текстовом поле
                                    tvCurrentTime.setText(formatDuration(currentPosition));

                                    // Обновление позиции SeekBar на основе текущего времени
                                    seekBar.setMax(totalDuration);
                                    seekBar.setProgress(currentPosition);
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //слушатель изменения позиции SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress); // Перемотка трека по позиции SeekBar
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Метод вызывается при начале перемещения ползунка SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Метод вызывается при окончании перемещения ползунка SeekBar
            }
        });
        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousTrack();
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextTrack();
            }
        });
    }
    //Свайп
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
    private void stopPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
    private String formatDuration(int duration) {
        int seconds = (duration / 1000) % 60;
        int minutes = (duration / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void playTrack(Track track) {
        if (track != null) {
            String trackUri = track.getTrackUri();
            if (trackUri != null) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(trackUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    tvSongTitle.setText(track.getTitle());
                    tvTotalTime.setText(track.getTime());
                    
                   // notificationHelper.showNotification(this, track.getTitle(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(PlayerActivity.this, "Трек недоступен. Переход к следующему треку.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("PlayerActivity", "Неверный объект отслеживания");
        }
    }

    private void playNextTrack() {
        ArrayList<Track> tracksList = getIntent().getParcelableArrayListExtra("trackList");
        if (!tracksList.isEmpty()) {
            if (currentTrackIndex < tracksList.size() - 1) {
                currentTrackIndex++;
            } else {
                currentTrackIndex = 0;
            }

            Track nextTrack = tracksList.get(currentTrackIndex);
            if (nextTrack != null && nextTrack.getTrackUri() != null) {
                playTrack(nextTrack);
            } else {
                Log.e("PlayerActivity", "Invalid track or trackUri is null for the next track");
            }
        } else {
            Log.d("PlayerActivity", "Empty tracks list");
        }
    }

    private void playPreviousTrack() {
        ArrayList<Track> tracksList = getIntent().getParcelableArrayListExtra("trackList");
        if (!tracksList.isEmpty()) {
            if (currentTrackIndex > 0) {
                currentTrackIndex--;
            } else {
                currentTrackIndex = tracksList.size() - 1;
            }

            Track previousTrack = tracksList.get(currentTrackIndex);
            if (previousTrack != null && previousTrack.getTrackUri() != null) {
                playTrack(previousTrack);
            } else {
                Log.e("PlayerActivity", "Invalid track or trackUri is null for the previous track");
            }
        } else {
            Log.d("PlayerActivity", "Empty tracks list");
        }
    }

    private void togglePlayPause() {
        //переключение между воспроизведением и паузой
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                ivPlayPause.setImageResource(R.drawable.arg_play);
            } else {
                mediaPlayer.start();
                ivPlayPause.setImageResource(R.drawable.arg_pause);
            }
            isPlaying = !isPlaying;
        }
    }

}