package com.example.muaupl;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashScreen extends AppCompatActivity {

    public static final int Request_Code_For_Permission = 1;
    public static final int CAMERA_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        // Проверяем разрешение на чтение файлов
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Если разрешение не предоставлено, запрашиваем его
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Request_Code_For_Permission);
        } else {
            // Разрешение уже предоставлено или запрашивалось ранее, переходим к следующей активности
            goToNextActivity();
        }
    }

    private void checkPermissionAndProceed() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Если разрешение не предоставлено, или было отклонено ранее, показываем диалог запрашивающий разрешение
            showPermissionDialog();
        } else {
            // Разрешение уже предоставлено, переходим к следующей активности
            goToNextActivity();
        }
    }

    private void showPermissionDialog() {
        // Проверяем, показывали ли уже окно запроса разрешения
        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Show the rational explanation if the user denied the permission previously
            AlertDialog.Builder rationaleBuilder = new AlertDialog.Builder(this);
            rationaleBuilder.setMessage("Для продолжения необходимо предоставить разрешение на чтение файлов. Предоставить разрешение?");
            rationaleBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Пользователь нажал "Да", запрашиваем разрешение
                    ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Request_Code_For_Permission);
                }
            });
            rationaleBuilder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Пользователь нажал "Нет", закрываем приложение
                    finish();
                }
            });
            rationaleBuilder.setCancelable(false);
            AlertDialog rationaleDialog = rationaleBuilder.create();
            rationaleDialog.show();
        } else {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Request_Code_For_Permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_Code_For_Permission) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение на чтение файлов предоставлено пользователем, переходим к следующей активности
                goToNextActivity();
            } else {
                // Разрешение на чтение файлов не предоставлено, показываем диалоговое окно с сообщением об отказе
                showPermissionDialog();
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение на камеру предоставлено пользователем, переходим к следующей активности
                goToNextActivity();
            } else {
                // Разрешение на камеру не предоставлено, показываем диалоговое окно с сообщением об отказе
                showCameraPermissionDeniedDialog();
            }
        }
    }

    private void goToNextActivity() {
        // Здесь можно запустить следующую активность
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showCameraPermissionDeniedDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Для использования камеры приложению необходимо разрешение на доступ к камере. Хотите предоставить разрешение?");
        dialogBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Пользователь нажал "Да", запрашиваем разрешение на камеру
                ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }
        });
        dialogBuilder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Пользователь нажал "Нет", закрываем приложение или делаем необходимые действия
                finish();
            }
        });
        dialogBuilder.setCancelable(false);
        AlertDialog permissionDialog = dialogBuilder.create();
        permissionDialog.show();
    }

}