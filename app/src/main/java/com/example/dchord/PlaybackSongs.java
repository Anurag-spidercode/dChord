package com.example.dchord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PlaybackSongs extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "songs.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "songs";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_ARTIST = "artist";
    private static final String COL_PATH = "path";

    public PlaybackSongs(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_ARTIST + " TEXT, " +
                COL_PATH + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 🔽 INSERT
    public boolean insertSong(String title, String artist, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_ARTIST, artist);
        values.put(COL_PATH, path);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // ✏️ UPDATE
    public boolean updateSong(int id, String newTitle, String newArtist, String newPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, newTitle);
        values.put(COL_ARTIST, newArtist);
        values.put(COL_PATH, newPath);
        int result = db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // 👁️ GET ALL SONGS
    public List<HistorySong> getAllSongs() {
        List<HistorySong> songList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTIST));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(COL_PATH));
                songList.add(new HistorySong(id, title, artist, path));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songList;
    }

    // 🔍 GET LAST HistorySong (e.g., for player)
    public HistorySong getLastPlayedSong() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTIST));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(COL_PATH));
            cursor.close();
            return new HistorySong(id, title, artist, path);
        }
        return null;
    }
}

