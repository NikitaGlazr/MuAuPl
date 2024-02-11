package com.example.muaupl;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashScreen extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_music_window);

        requestMediaFilePermission();
    }

    private void requestMediaFilePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, PERMISSION_REQUEST_CODE);
        } else {
            // Если разрешение уже предоставлено, перейти к следующей активности
            Intent intent = new Intent(SplashScreen.this, MainActivityMusicWindow.class);
            startActivity(intent);
            finish(); // Закрыть текущую активность
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено, можно продолжить выполнение операций
                Intent intent = new Intent(SplashScreen.this, MainActivityMusicWindow.class);
                startActivity(intent);
                finish(); // Закрыть текущую активность
            } else {
                // Разрешение было отклонено, показать пользователю уведомление об этом
                Toast.makeText(this, "Разрешение на чтение медиафайлов не было предоставлено", Toast.LENGTH_SHORT).show();
                finish(); // Закрыть текущую активность
            }
        }
    }
}