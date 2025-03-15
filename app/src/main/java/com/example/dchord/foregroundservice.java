package com.example.dchord;

import static com.example.dchord.adapter.filepathurl;
import static com.example.dchord.adapter.filepathurl2;
import static com.example.dchord.adapter.filepathurl3;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.dchord.Singleton;

import java.io.IOException;

public class foregroundservice extends Service{
    String titleretrive;
    String artistretrive;
    String filepathretrive;

    int minute;
    int second;

    static int durationprogress;

    int currentminute;
    int currentsecond;


    Intent intent = new Intent("com.example.dchord.broadcastreceiver");

    static MediaPlayer mediaPlayer;

    private Messenger activitymessenger = null;
    //    private final Messenger servicemesseger = new Messenger(new IncomingHandler());
    private int maxdurationretriever;
    private boolean play = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Ensures this runs only on Android 8.0+
            NotificationChannel channel = new NotificationChannel(
                    "CHANNEL_ID", // Unique ID for the channel
                    "Music Playback", // Visible name in system settings
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel); // Update notification
            }
        }


    }
    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle(titleretrive != null ? titleretrive : titleretrive)
                .setContentText(artistretrive != null ? artistretrive : artistretrive)
                .setSmallIcon(R.drawable.app_icon)
                .setOngoing(true) // Prevents accidental dismissal
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Keeps it visible in the drawer
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.play, "Play", getPlaybackAction("play"))
                .addAction(R.drawable.pause, "Pause", getPlaybackAction("pause"));
//                .addAction(R.drawable.stop, "Stop", getPlaybackAction("stop"));

        builder.addAction(R.drawable.play, "Play", getPlaybackAction("play"))
                .addAction(R.drawable.pause, "Pause", getPlaybackAction("pause"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+
            builder.setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE);
        }

        return builder.build();
    }

    private PendingIntent getPlaybackAction(String action) {
        Intent intent = new Intent(this, foregroundservice.class);
        intent.setAction(action);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.hasExtra(filepathurl3)){
            filepathretrive = intent.getStringExtra(filepathurl3);
            titleretrive = intent.getStringExtra(filepathurl);
            artistretrive = intent.getStringExtra(filepathurl2);


            if (filepathretrive == null || filepathretrive.isEmpty()) {
                stopSelf();
                return START_NOT_STICKY;
            }

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(filepathretrive);
                mediaPlayer.prepare();
                maxdurationretriever = mediaPlayer.getDuration();
                Singleton.getInstance().setMaxduration(maxdurationretriever);

                Notification notification = createNotification();
                startForeground(1,notification);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopmusic();
                        stopSelf();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(intent != null && intent.getAction() != null){
            switch(intent.getAction()){
                case "play":
                    playmusic();
                    break;
                case "pause":
                    pausemusic();
                    break;
                case "stop":
                    stopmusic();
                    stopSelf();
                    break;
                case "next":
                    nextsong();
            }
        }
        return START_STICKY;
    }

    private void nextsong() {

    }


    private void playmusic() {
        if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
            Singleton.getInstance().setUpdate(true);
            mediaPlayer.start();
            play = false;
        }
    }

    private void pausemusic(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            Singleton.getInstance().setUpdate(false);
            mediaPlayer.pause();
            play = true;
        }
    }

    private void stopmusic(){
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
            stopForeground(true);
            stopSelf();
            play = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            stopmusic();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // Stop the music and the service when the app is removed from recents.
        stopmusic();
        stopSelf();
        super.onTaskRemoved(rootIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}




