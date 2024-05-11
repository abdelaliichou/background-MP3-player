package com.example.mp3background;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    RecyclerView recycler ;
    com.example.ilyastp5.adapter adapter;
    ArrayList<Music> musics;
    private MusicDBHelper dbHelper;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // refresh the list when getting back from the music activity
        settingRecycler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        settingRecycler();
    }

    private void settingRecycler(){
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        MusicDBHelper dbHelper = new MusicDBHelper(this);

        musics = dbHelper.getAllFavorites();

        adapter = new com.example.ilyastp5.adapter(this, musics);
        recycler.setAdapter(adapter);
    }

}