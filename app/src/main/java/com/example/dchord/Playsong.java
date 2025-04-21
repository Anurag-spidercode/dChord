//package com.example.dchord;
//
//import static com.example.dchord.adapter.filepathurl;
//import static com.example.dchord.adapter.filepathurl2;
//import static com.example.dchord.adapter.filepathurl3;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.lifecycle.ViewModelStoreOwner;
//
//public class Playsong extends AppCompatActivity {
//
//    Intent intent;
//    Intent intent2;
//    TextView title, artist, startingtime, endingtime;
//
//
//    String filepathretrive;
//    String titleretrive;
//    String artistretrive;
//    ImageButton playbutton, pausebutton, previousbutton, nextbutton;
//
//    Runnable updateSeekBarTask;
//
//    final Handler handler = new Handler(Looper.getMainLooper());
//
//    SeekBar progressbar;
//    Viewmodel viewmodel;
//
//    String progressModified;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        viewmodel = new ViewModelProvider(this).get(Viewmodel.class);
//
//
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.playsong);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        title = findViewById(R.id.titleplay);
//        artist = findViewById(R.id.artistplay);
//        playbutton = findViewById(R.id.play);
//        pausebutton = findViewById(R.id.pause);
//        startingtime = findViewById(R.id.startingtime);
//        endingtime = findViewById(R.id.endingtime);
//        progressbar = findViewById(R.id.progressBar);
//        previousbutton = findViewById(R.id.previous);
//        nextbutton = findViewById(R.id.next);
//
//
//        viewmodel.getsongName().observe(this, name ->{
//            name = Singleton.getInstance().getTitle();
//            if(name.length() > 15)
//            {
//                name = name.substring(0,15) + "...";
//            }
//            title.setText(name);
//        });
//
//        viewmodel.getArtist().observe(this, artistname ->{
//            artistname = Singleton.getInstance().getArtist();
//            if(artistname.length() > 15)
//            {
//                artistname = artistname.substring(0,14) + "...";
//            }
//            artist.setText(artistname);
//        });
//
//        viewmodel.getCurrentIndex().observe(this, index ->{
//            if(index != null){
//                String name = Singleton.getInstance().getTitle();
//                if(name.length() > 15)
//                {
//                    name = name.substring(0,15) + "...";
//                }
//                title.setText(name);
//
//                String artistname = Singleton.getInstance().getArtist();
//                if(artistname.length() > 15)
//                {
//                    artistname = artistname.substring(0,14) + "...";
//                }
//                artist.setText(artistname);
//            }
//        });
//
//        progressbar.setMax((int) Singleton.getInstance().getMaxduration());
//
//        updateSeekBarTask = new Runnable() {
//
////            @Override
////            public void run() {
////                if (foregroundservice.mediaPlayer != null  && mediaPlayer.isPlaying()) {
////
////                    int currentPos = mediaPlayer.getCurrentPosition();
////                    progressbar.setProgress(currentPos);
////                    int minute = currentPos / 60000;
////                    int second = (currentPos % 60000) / 1000;
////
////                    startingtime.setText(String.format("%02d:%02d", minute, second));
////                    handler.postDelayed(this, 0);
////                }
////            }
//        };
//        handler.post(updateSeekBarTask);
//
//        progressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                if(fromUser && mediaPlayer != null)
////                {
////                    mediaPlayer.seekTo(progress);
////                }
////            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                handler.post(updateSeekBarTask);
//            }
//        });
//
//
//
//
//        long duration = Singleton.getInstance().getMaxduration();
//        int minute = (int) (duration / 60000);
//        int second = (int) ((duration % 60000) / 1000);
//        String durationmodified = String.format("%02d:%02d",minute,second);
//        endingtime.setText(durationmodified);// setting max duration
//        playbutton.setVisibility(View.GONE);
//
//
//
//        intent = new Intent(this, foregroundservice.class);
//        playbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setAction("play");
//                playbutton.setVisibility(View.GONE);
//                pausebutton.setVisibility(View.VISIBLE);
//                startService(intent);
//            }
//        });
//
//        pausebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setAction("pause");
//                pausebutton.setVisibility(View.GONE);
//                playbutton.setVisibility(View.VISIBLE);
//                startService(intent);
//            }
//        });
//
//        nextbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("DEBUG", "Next pressed. Old index=" + viewmodel.getCurrentIndex().getValue());
//                viewmodel.playNextSong();
//                if (viewmodel == null) {
//                    Log.e("com.example.dchord", "viewModel is NULL in playsong()");
//                } else {
//                    Log.d("com.example.dchord", "viewModel is NOT NULL in playsong()");
//                }
//
//                int index = viewmodel.getCurrentIndex().getValue();
//                viewmodel.setCurrentIndex(index+1);
//                Log.d("DEBUG", "New index=" + viewmodel.getCurrentIndex().getValue());
//                Log.d("DEBUG", "New path=" + Singleton.getInstance().getSongpath());
//                intent.setAction("change");
//                startService(intent);
//            }
//        });
//
//        previousbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewmodel.playPreviousSong();
//                intent.setAction("change");
//                startService(intent);
//            }
//        });
//
//
//    }
//
//        @Override
//        protected void onDestroy() {
//            super.onDestroy();
//            stopSeekBarUpdate();
//            handler.removeCallbacksAndMessages(null);
//        }
//
//    private void stopSeekBarUpdate() {
//        handler.removeCallbacks(updateSeekBarTask);
//    }
//
//
//
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            boolean update = Singleton.getInstance().getUpdate();
//            if(update)
//            {
//                playbutton.setVisibility(View.GONE);
//                pausebutton.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                pausebutton.setVisibility(View.GONE);
//                playbutton.setVisibility(View.VISIBLE);
//            }
//        } else {
//
//        }
//    }
//
//
//}