package com.example.mp3background;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MusicDBHelper extends SQLiteOpenHelper {
    // Database Name
    private static final String DATABASE_NAME = "music_db";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_MUSIC = "music";

    // Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_FAV = "fav";
    private static final String KEY_MP3 = "mp3";
    private static final String KEY_NAME = "name";

    public MusicDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MUSIC_TABLE = "CREATE TABLE " + TABLE_MUSIC + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MP3 + " INTEGER,"
                + KEY_FAV + " BOOLEAN,"
                + KEY_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_MUSIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);

        // Create tables again
        onCreate(db);
    }

    // Getting single music
    @SuppressLint("Range")
    public Music getMusic(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MUSIC, new String[]{KEY_ID,
                        KEY_MP3,KEY_FAV, KEY_NAME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Music music = new Music();
        music.setMp3(Integer.parseInt(cursor.getString(1)));
        music.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
        music.setFav(cursor.getInt(cursor.getColumnIndex(KEY_FAV)) == 1);
        music.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));

        // return music
        return music;
    }

    // Adding new music
    public long addMusic(Music music) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MP3, music.getMp3());
        values.put(KEY_NAME, music.getName());
        values.put(KEY_FAV, music.isFav());

        // Inserting Row
        long id = db.insert(TABLE_MUSIC, null, values);
        // Closing database connection
        db.close();
        return id;
    }

    // Updating single music
    public int updateMusic(Music music) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(KEY_FAV, music.isFav());

        // updating row
        return db.update(TABLE_MUSIC, values, KEY_ID + "=?",
                new String[]{String.valueOf(music.getId())});
    }

    // Getting All music
    @SuppressLint("Range")
    public ArrayList<Music> getAllMusic() {
        ArrayList<Music> musicList = new ArrayList<Music>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MUSIC;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Music music = new Music();
                music.setMp3(Integer.parseInt(cursor.getString(1)));
                music.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                music.setFav(cursor.getInt(cursor.getColumnIndex(KEY_FAV)) == 1);
                music.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                // Adding music to list
                musicList.add(music);

            } while (cursor.moveToNext());
        }

        // return music list
        return musicList;
    }

    public ArrayList<Music> getAllFavorites(){
        ArrayList<Music> musics = getAllMusic();
        ArrayList<Music> newlist = new ArrayList<>();

        for (int i = 0; i < musics.size(); i++){
            if (musics.get(i).isFav()) newlist.add(musics.get(i));
        }

        return newlist;
    }


    // Deleting single music
    public void deleteMusic(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MUSIC, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
