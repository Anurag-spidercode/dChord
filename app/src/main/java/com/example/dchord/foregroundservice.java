package com.example.dchord;


import static com.example.dchord.adapter.filepathurl;
import static com.example.dchord.adapter.filepathurl2;
import static com.example.dchord.adapter.filepathurl3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.Adapter;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class foregroundservice extends Service {
    private MediaSessionCompat mediaSession;
    private static final String CHANNEL_ID = "music_channel";

    String title;
    String artist;
    String path;
    int index = 0;

    public static MediaPlayer  mediaPlayer;
    Singleton singleton = Singleton.getInstance();
    private List<data> list;
    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("ACTION_PLAY_PAUSE".equals(action)) {
                play();
            } else if ("ACTION_NEXT".equals(action)) {
                next();
            } else if ("ACTION_PREV".equals(action)) {
                previous();
            } else if ("ACTION_PAUSE_PAUSE".equals(action)) {
                pause();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_PLAY_PAUSE");
        filter.addAction("ACTION_NEXT");
        filter.addAction("ACTION_PREV");
        filter.addAction("ACTION_PAUSE_PAUSE");
        registerReceiver(notificationReceiver, filter);

        mediaSession = new MediaSessionCompat(this, "MusicSession");
        createNotificationChannel();

        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        list = singleton.getSongList();
        if(intent != null) {
            String titleDummy = intent.getStringExtra(filepathurl);
            String artistDummy = intent.getStringExtra(filepathurl2);
            path = intent.getStringExtra(filepathurl3);
            String action = intent.getStringExtra("action");
            if (action != null) {
                switch (action) {
                    case "start":
                        play();
                        break;
                    case "pause":
                        pause();
                        break;
                    case "next":
                        next();
                        break;
                    case "previous":
                        previous();
                        break;
                }
            }

            setname(titleDummy, artistDummy);

//            if(titleDummy != null && artistDummy != null) {
//                if (titleDummy.length() > 20) {
//                    title = titleDummy.substring(0, 20) + "...";
//                } else {
//                    title = titleDummy;
//                }
//
//                if (artistDummy.length() > 35) {
//                    artist = artistDummy.substring(0, 35) + "...";
//                } else {
//                    artist = artistDummy;
//                }
//                singleton.setTitle(title);
//                singleton.setArtist(artist);
//                singleton.setSongpath(path);
//            }


            if (path != null && !path.isEmpty()) {
                playMusic(path);
            }
            startForeground(1, createNotification());
        }
        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void playMusic(String path){
        try {
            if (mediaPlayer.isPlaying() || singleton.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            else{
                mediaPlayer.reset();
            }
            mediaPlayer.setDataSource(this, Uri.parse(path));
            mediaPlayer.prepare();
            mediaPlayer.start();
            singleton.setMusicStart(true);
            singleton.setPlaying(true);
            mediaPlayer.setOnCompletionListener(mp -> {
                next();
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void play(){
        mediaPlayer.start();
        singleton.setPlaying(true);
        updateNotification();
    }

    private void pause(){
        mediaPlayer.pause();
        singleton.setPlaying(false);
        updateNotification();
    }

    private void next(){
        if(list != null && !list.isEmpty()) {
            index = singleton.getPosition();
            if (index < list.size() - 1) {
                index++;
                singleton.setPosition(index);
                String path = list.get(index).getFilePath();
                String title = list.get(index).getTitle();
                String artist = list.get(index).getArtist();
                singleton.setSongpath(path);
                setname(title,artist);
                playMusic(path);
                updateNotification();
            }
        }
    }

    private void previous(){
        if(list != null && !list.isEmpty()) {
            index = singleton.getPosition();
            if (index > 0) {
                index--;
                singleton.setPosition(index);
                String path = list.get(index).getFilePath();
                String title = list.get(index).getTitle();
                String artist = list.get(index).getArtist();
                singleton.setSongpath(path);
                setname(title,artist);
                playMusic(path);
                updateNotification();
            }
        }
    }

    private void setname(String titleDummy, String artistDummy) {
        if(titleDummy != null && artistDummy != null) {
            if (titleDummy.length() > 20) {
                title = titleDummy.substring(0, 20) + "...";
            } else {
                title = titleDummy;
            }

            if (artistDummy.length() > 35) {
                artist = artistDummy.substring(0, 35) + "...";
            } else {
                artist = artistDummy;
            }
            singleton.setTitle(title);
            singleton.setArtist(artist);
            singleton.setSongpath(path);
            singleton.getInstance().setLiveTitle(title);
            singleton.getInstance().setLiveArtist(artist);
            updateNotification();
        }
    }

    private Notification createNotification() {
        RemoteViews notificationlayout = new RemoteViews(getPackageName(), R.layout.notification_collapsed);

        notificationlayout.setTextViewText(R.id.libraryTitle, title);
        notificationlayout.setTextViewText(R.id.libraryArtist, artist);

        PendingIntent prevIntent = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_PREV"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent playPauseIntent = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_PLAY_PAUSE"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent nextIntent = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_NEXT"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pausePauseIntent = PendingIntent.getBroadcast(this, 0, new Intent("ACTION_PAUSE_PAUSE"), PendingIntent.FLAG_IMMUTABLE);

        notificationlayout.setOnClickPendingIntent(R.id.previouslist, prevIntent);
        notificationlayout.setOnClickPendingIntent(R.id.playlist, playPauseIntent);
        notificationlayout.setOnClickPendingIntent(R.id.pauselist, pausePauseIntent);
        notificationlayout.setOnClickPendingIntent(R.id.nextlist,  nextIntent);

        if (mediaPlayer != null && singleton.isPlaying()) {
            notificationlayout.setViewVisibility(R.id.playlist, View.GONE);  // Hide Play button
            notificationlayout.setViewVisibility(R.id.pauselist, View.VISIBLE);
        }
        else {
            notificationlayout.setViewVisibility(R.id.playlist, View.VISIBLE);  // Show Play button
            notificationlayout.setViewVisibility(R.id.pauselist, View.GONE);  // Hide Pause button
        }
        Intent intent = new Intent(this, MainActivity.class); // or your desired Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.defaultimg)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationlayout)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();

        return notification;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Playback",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void updateNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, createNotification());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        unregisterReceiver(notificationReceiver);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        unregisterReceiver(notificationReceiver);
        stopForeground(true);
        stopSelf();
    }
}




