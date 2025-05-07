package com.example.dchord;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE_STORAGE = 123;
    private static final int REQUEST_PERMISSION_CODE_NOTIFICATION = 124;
    ImageView homeOuter, homeFilled, heartOuter, heartFilled, playlistOuter, playlistFilled, historyOuter, historyFilled, timeline_outline, timeline_filled, all_outline, all_filled;

    TextView allsongs, history;
    ConstraintLayout historylayout, alllayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();

        homeOuter = findViewById(R.id.homeOuter);
        heartOuter = findViewById(R.id.heartOuter);
        playlistOuter = findViewById(R.id.playlistOuter);
        historyOuter = findViewById(R.id.historyOuter);

        allsongs = findViewById(R.id.all);
        history =  findViewById(R.id.history);

        homeFilled = findViewById(R.id.homeFilled);
        heartFilled = findViewById(R.id.heartFilled);
        playlistFilled = findViewById(R.id.playlistFilled);
        historyFilled = findViewById(R.id.historyFilled);

        timeline_filled= findViewById(R.id.timeline_filled);
        timeline_outline= findViewById(R.id.timeline_outline);
        historylayout = findViewById(R.id.history_layout);

        all_filled= findViewById(R.id.all_filled);
        all_outline=findViewById(R.id.all_outline);
        alllayout=findViewById(R.id.all_layout);


        fragmentswap(new MusicList());
        buttonclick(5);

        alllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new MusicList());
                buttonclick(5);
            }
        });

        historylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new history());
                buttonclick(6);
            }
        });

        homeOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new MusicList());
                buttonclick(1);
            }
        });

        homeFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new MusicList());
                buttonclick(1);
            }
        });

        heartOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new fragmnetFavourate());
                buttonclick(2);
            }
        });

        heartFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new fragmnetFavourate());
                buttonclick(2);
            }
        });

        playlistOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new history());
                buttonclick(3);
            }
        });

        playlistFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new history());
                buttonclick(3);
            }
        });

        historyOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new history());
                buttonclick(4);
            }
        });

        historyFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentswap(new history());
                buttonclick(4);
            }
        });
    }


    public void fragmentswap(Fragment fragmnet){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragmnet).commit();
    }

    public void buttonclick(int swap){
        switch(swap){
            case 1 :
                homeOuter.setVisibility(ImageView.INVISIBLE);
                heartOuter.setVisibility(ImageView.VISIBLE);
                playlistOuter.setVisibility(ImageView.VISIBLE);
                historyOuter.setVisibility(ImageView.VISIBLE);

                homeFilled.setVisibility(ImageView.VISIBLE);
                heartFilled.setVisibility(ImageView.INVISIBLE);
                playlistFilled.setVisibility(ImageView.INVISIBLE);
                historyFilled.setVisibility(ImageView.INVISIBLE);

                break;
            case 2 :
                homeOuter.setVisibility(ImageView.VISIBLE);
                heartOuter.setVisibility(ImageView.INVISIBLE);
                playlistOuter.setVisibility(ImageView.VISIBLE);
                historyOuter.setVisibility(ImageView.VISIBLE);

                homeFilled.setVisibility(ImageView.INVISIBLE);
                heartFilled.setVisibility(ImageView.VISIBLE);
                playlistFilled.setVisibility(ImageView.INVISIBLE);
                historyFilled.setVisibility(ImageView.INVISIBLE);
                break;
            case 3 :
                homeOuter.setVisibility(ImageView.VISIBLE);
                heartOuter.setVisibility(ImageView.VISIBLE);
                playlistOuter.setVisibility(ImageView.INVISIBLE);
                historyOuter.setVisibility(ImageView.VISIBLE);

                homeFilled.setVisibility(ImageView.INVISIBLE);
                heartFilled.setVisibility(ImageView.INVISIBLE);
                playlistFilled.setVisibility(ImageView.VISIBLE);
                historyFilled.setVisibility(ImageView.INVISIBLE);
                break;
            case 4 :
                homeOuter.setVisibility(ImageView.VISIBLE);
                heartOuter.setVisibility(ImageView.VISIBLE);
                playlistOuter.setVisibility(ImageView.VISIBLE);
                historyOuter.setVisibility(ImageView.INVISIBLE);

                homeFilled.setVisibility(ImageView.INVISIBLE);
                heartFilled.setVisibility(ImageView.INVISIBLE);
                playlistFilled.setVisibility(ImageView.INVISIBLE);
                historyFilled.setVisibility(ImageView.VISIBLE);
                break;

            case 5:
                allsongs.setTextColor(Color.parseColor("#C6C6C6"));//white
                history.setTextColor(Color.parseColor("#49454F"));//grey
                timeline_outline.setVisibility(View.VISIBLE);
                timeline_filled.setVisibility(View.INVISIBLE);

                all_outline.setVisibility(View.INVISIBLE);
                all_filled.setVisibility(View.VISIBLE);
                break;
            case 6:
                allsongs.setTextColor(Color.parseColor("#49454F"));//grey
                history.setTextColor(Color.parseColor("#C6C6C6"));//white
                timeline_outline.setVisibility(View.INVISIBLE);
                timeline_filled.setVisibility(View.VISIBLE);

                all_outline.setVisibility(View.VISIBLE);
                all_filled.setVisibility(View.INVISIBLE);
                break;
        }
    }


    private void checkAndRequestPermissions() {
        // For Android 13+ (POST_NOTIFICATIONS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_PERMISSION_CODE_NOTIFICATION);
            }
        }

        // For Android versions below Android 13, check READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE_STORAGE);
        }
    }

}



