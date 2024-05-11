package com.example.mp3background;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.ilyastp5.adapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler ;
    com.example.ilyastp5.adapter adapter;
    ArrayList<Music> musics;
    private MusicDBHelper dbHelper;
    FloatingActionButton fab;

    ImageView favorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialisation();
        settingRecycler();
        onClicks();

    }

    private void onClicks(){
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(v);
            }
        });
    }

    private void showInputDialog(View anchorView) {

        View dialogView = this.getLayoutInflater().inflate(R.layout.popup_layout, null);

        EditText editText = dialogView.findViewById(R.id.editText);
        Button submitButton = dialogView.findViewById(R.id.button);

        // Create a PopupWindow with match_parent width and wrap_content height
        PopupWindow popupWindow = new PopupWindow(dialogView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setElevation(50f);

        // Show the popup window at the bottom of the anchor view
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = editText.getText().toString();

                if (inputText.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a TITLE!", Toast.LENGTH_SHORT).show();
                } else {
                    // Adding the note to the database
                    Music note = new Music();
                    note.setName(inputText);
                    note.setMp3(R.raw.music);
                    note.setFav(false);
                    long id = dbHelper.addMusic(note);
                    note.setId(id);
                    settingRecycler();

                    popupWindow.dismiss();
                }
            }
        });
    }

    private void initialisation() {
        favorite = findViewById(R.id.save);
        fab = findViewById(R.id.floating);
        recycler = findViewById(R.id.recycler);
        dbHelper = new MusicDBHelper(this);
    }

    private void settingRecycler(){
        recycler.setLayoutManager(new LinearLayoutManager(this));

        musics = dbHelper.getAllMusic();

        adapter = new adapter(this, musics);
        recycler.setAdapter(adapter);
    }

    private ArrayList<Music> testList(){
        ArrayList<Music> musics = new ArrayList<>();
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));
        musics.add(new Music(1,R.raw.music, "Music 1", false));

        return musics;

    }
}