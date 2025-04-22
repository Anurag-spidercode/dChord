package com.example.dchord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class favoriteSong extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "favorites";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_ARTIST = "artist";
    private static final String COL_PATH = "path";

    public favoriteSong(Context context) {
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ✅ Insert
    public boolean insertFavorite(String title, String artist, String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_ARTIST, artist);
        values.put(COL_PATH, path);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // ❌ Delete by path
    public boolean deleteFavoriteByPath(String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_PATH + " = ?", new String[]{path});
        return result > 0;
    }

    // 👀 Get all favorites
    public List<HistorySong> getAllFavorites() {
        List<HistorySong> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTIST));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(COL_PATH));
                favorites.add(new HistorySong(id, title, artist, path));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favorites;
    }

    // 🕵️ Optional: Check if a song is already in favorites
    public boolean isFavorite(String path) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_PATH + " = ?", new String[]{path}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
}
