package com.example.mp3background;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MusicActivity extends AppCompatActivity {


    TextView description;
    Button addFav, deleteFav;
    long id;
    MediaPlayer mediaPlayer;
    MusicDBHelper dbHelper;
    Music music;

    Boolean backPressed = false ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        backPressed = false ;
        addFav = findViewById(R.id.button);
        deleteFav = findViewById(R.id.button2);
        dbHelper = new MusicDBHelper(this);

        settingData();
        settingAUDIO();
        onClick();


        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.release();
            mediaPlayer = null;
            // Start the background service passing the current position
            Intent serviceIntent = new Intent(this, MusicService.class);
            serviceIntent.putExtra("CURRENT_POSITION", currentPosition);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            }
        }

    }

    private void onClick(){
        addFav.setOnClickListener(view -> {

            if (music.isFav()) {
                Toast.makeText(MusicActivity.this, "Item already in favorite !", Toast.LENGTH_SHORT).show();
                return;
            }

            music.setFav(true);
            int result = dbHelper.updateMusic(music);

            if ( result == 0 ) {
                Toast.makeText(MusicActivity.this, "Error while adding item to favorite !", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(MusicActivity.this, "Item added to favorite successfully !", Toast.LENGTH_SHORT).show();

        });
        deleteFav.setOnClickListener(view -> {

            if (!music.isFav()) {
                // item is not favorite
                return;
            }

            music.setFav(false);
            int result = dbHelper.updateMusic(music);

            if ( result == 0 ) {
                Toast.makeText(MusicActivity.this, "Error while removing item from favorite !", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(MusicActivity.this, "Item removed from favorite successfully !", Toast.LENGTH_SHORT).show();
        });
    }

    private void settingData(){
            description = findViewById(R.id.discText);
            // getting data from previose screen
            String data = getIntent().getStringExtra("text");
            id = getIntent().getLongExtra("id", 0);
            music = dbHelper.getMusic(id);
            description.setText(data);
    }

    private void settingAUDIO(){
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("DESTROOOOOOOOOOOOOOOOOOOOOY","yes");

//        if (!backPressed){
//
//
//            // The activity is being finished (e.g., back button pressed, app closed)
//            // You can release the MediaPlayer resources here
//            if (mediaPlayer != null) {
//                int currentPosition = mediaPlayer.getCurrentPosition();
//                mediaPlayer.release();
//                mediaPlayer = null;
//                // Start the background service passing the current position
//                Intent serviceIntent = new Intent(this, MusicService.class);
//                serviceIntent.putExtra("CURRENT_POSITION", currentPosition);
//                startService(serviceIntent);
//            }
//
//
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed = true ;
        // Release the MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}