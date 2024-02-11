package com.example.muaupl;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivityMusicWindow extends AppCompatActivity {
    private Toolbar mToolbar;
    public static final int SORT_BY_NAME_ID = R.id.sort_by_name;
    public static final int SORT_BY_DATE_ID = R.id.sort_by_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_music_window);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mToolbar.setLogo(R.drawable.icomusic);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        MenuItem searchItem = menu.findItem(R.id.action_search_input);
        EditText editTextSearch = searchItem.getActionView().findViewById(R.id.edit_text_search);

        //изменение цвета кнопки для поиска
        Drawable searchIcon = item.getIcon();
        if (searchIcon != null) {
            searchIcon.mutate();
            searchIcon.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            item.setIcon(searchIcon);
        }


        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Расширение поля для ввода текста
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Сворачивание поля для ввода текста
                return true;
            }
        });

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Действия при нажатии на кнопку "Готово" на клавиатуре
                String searchText = editTextSearch.getText().toString();
                performSearch(searchText);
                return true;
            }
            return false;
        });
        return true;
    }
    private void performSearch(String query) {
        // Обработка поискового запроса
        // Здесь можно добавить логику для поиска и отображения результатов
        Toast.makeText(this, "Поиск: " + query, Toast.LENGTH_SHORT).show();
    }
}
