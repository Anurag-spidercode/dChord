package com.example.dchord;

import static com.example.dchord.adapter.filepathurl;
import static com.example.dchord.adapter.filepathurl2;
import static com.example.dchord.adapter.filepathurl3;
import static com.example.dchord.foregroundservice.mediaPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class Playsong extends AppCompatActivity {

    Intent intent;
    Intent intent2;
    TextView title, artist, startingtime, endingtime;


    String filepathretrive;
    String titleretrive;
    String artistretrive;
    ImageButton playbutton, pausebutton;

    Runnable updateSeekBarTask;

    final Handler handler = new Handler(Looper.getMainLooper());

    SeekBar progressbar;
    Viewmodel viewmodel;

    String progressModified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewmodel = new ViewModelProvider(this).get(Viewmodel.class);

        EdgeToEdge.enable(this);
        setContentView(R.layout.playsong);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title = findViewById(R.id.titleplay);
        artist = findViewById(R.id.artistplay);
        playbutton = findViewById(R.id.play);
        pausebutton = findViewById(R.id.pause);
        startingtime = findViewById(R.id.startingtime);
        endingtime = findViewById(R.id.endingtime);
        progressbar = findViewById(R.id.progressBar);


            viewmodel.setSongname(Singleton.getInstance().getTitle());
            viewmodel.setArtist(Singleton.getInstance().getArtist());

        progressbar.setMax((int) Singleton.getInstance().getMaxduration());


        updateSeekBarTask = new Runnable() {

            @Override
            public void run() {
                if (foregroundservice.mediaPlayer != null) {

                    int currentPos = mediaPlayer.getCurrentPosition();
                    progressbar.setProgress(currentPos);
                    int minute = currentPos / 60000;
                    int second = (currentPos % 60000) / 1000;

                    startingtime.setText(String.format("%02d:%02d", minute, second));
                    handler.postDelayed(this, 0);
                }
            }
        };
        handler.post(updateSeekBarTask);

        progressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser && mediaPlayer != null)
                {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.post(updateSeekBarTask);
            }
        });




        long duration = Singleton.getInstance().getMaxduration();
        int minute = (int) (duration / 60000);
        int second = (int) ((duration % 60000) / 1000);
        String durationmodified = String.format("%02d:%02d",minute,second);
        endingtime.setText(durationmodified);// setting max duration

        playbutton.setVisibility(View.GONE);

//        String Title = Singleton.getInstance().getTitle();
//        String Artist = Singleton.getInstance().getArtist();

        viewmodel.getsongName().observe(this, new Observer<String>(){
            @Override
            public void onChanged(String s) {
                if(s.length() > 15)
                {
                    s = s.substring(0,15) + "...";
                }
                title.setText(s);
            }
        });

        viewmodel.getArtist().observe(this, new Observer<String>(){
            @Override
            public void onChanged(String s) {
                if(s.length() > 15)
                {
                    s = s.substring(0,14) + "...";
                }
                artist.setText(s);
            }
        });

        intent = new Intent(this, foregroundservice.class);

        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setAction("play");
                playbutton.setVisibility(View.GONE);
                pausebutton.setVisibility(View.VISIBLE);
                startService(intent);
            }
        });

        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setAction("pause");
                pausebutton.setVisibility(View.GONE);
                playbutton.setVisibility(View.VISIBLE);
                startService(intent);
            }
        });


    }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            stopSeekBarUpdate();
            handler.removeCallbacksAndMessages(null);
        }

    private void stopSeekBarUpdate() {
        handler.removeCallbacks(updateSeekBarTask);
    }



        @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);  // Update intent reference

        // Extract new data (if any) and update UI
        updateUIFromIntent(intent);
    }

    private void updateUIFromIntent(Intent intent) {
        String newTitle = intent.getStringExtra("title");
        String newArtist = intent.getStringExtra("artist");

        if (newTitle != null) title.setText(newTitle);
        if (newArtist != null) artist.setText(newArtist);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            boolean update = Singleton.getInstance().getUpdate();
            if(update == true)
            {
                playbutton.setVisibility(View.GONE);
                pausebutton.setVisibility(View.VISIBLE);
            }
            else
            {
                pausebutton.setVisibility(View.GONE);
                playbutton.setVisibility(View.VISIBLE);
            }
        } else {

        }
    }


}