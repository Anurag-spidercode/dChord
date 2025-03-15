package com.example.dchord;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class MusicFetch {
    public static List<data> fetchAllSongs(ContentResolver contentResolver){
        List<data> list = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] fetch = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        Cursor cursor = contentResolver.query(uri, fetch, null, null, null);

        if(cursor != null && cursor.moveToFirst())
        {
            do{
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                list.add(new data(title, artist, filePath, duration));
            }while(cursor.moveToNext());
            cursor.close();
        }
        return list;
    }
}
