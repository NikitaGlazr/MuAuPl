package com.example.muaupl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MainActivityMusicWindow extends AppCompatActivity {
    private Toolbar mToolbar;
    private boolean isSearchBarOpened = false;
    MenuItem searchItem;
    MenuItem item;
    EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_music_window);

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Создание адаптера ViewPager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TracksFragment(), "Треки");
        viewPagerAdapter.addFragment(new PlaylistsFragment(), "Плейлисты");
        // Добавьте другие фрагменты, если необходимо

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mToolbar.setLogo(R.drawable.icomusic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        searchItem = menu.findItem(R.id.action_search_input);
        item = menu.findItem(R.id.action_search);
        editTextSearch = searchItem.getActionView().findViewById(R.id.edit_text_search);
        editTextSearch.setVisibility(View.INVISIBLE);

        item.setOnMenuItemClickListener(menuItem -> {
            if (!isSearchBarOpened) {
                isSearchBarOpened = true;
                editTextSearch.requestFocus();
                item.setIcon(R.drawable.iconsclose);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
            } else {
                isSearchBarOpened = false;
                editTextSearch.setText("");
                item.setIcon(R.drawable.iconsearch);
                searchItem.collapseActionView();
                editTextSearch.clearFocus();
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

    private void performSearch(String query) {
        Toast.makeText(this, "Поиск: " + query, Toast.LENGTH_SHORT).show();

        isSearchBarOpened = false;
        editTextSearch.setVisibility(View.VISIBLE);
        editTextSearch.requestFocus();
        item.setIcon(R.drawable.iconsclose);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
        editTextSearch.requestFocus();
    }
}