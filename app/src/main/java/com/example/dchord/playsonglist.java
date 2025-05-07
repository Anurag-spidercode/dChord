package com.example.dchord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.dchord.foregroundservice;


public class playsonglist extends Fragment {
    Singleton singleton = Singleton.getInstance();
    TextView title , artist, startingtime, endtime;
    SeekBar seekBar;

    favoriteSong fav;

    private List<data> list = new ArrayList<>();
    ImageView previous, play, pause, next, like, dislike, image;
    Context context;

    private final BroadcastReceiver playbackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("ACTION_PLAY_PAUSE".equals(action)) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
            } else if ("ACTION_PAUSE_PAUSE".equals(action)) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
            }
        }
    };


    private final BroadcastReceiver songChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            String newPath = intent.getStringExtra("path");
            // directly update with the fresh path:
            Glide.with(requireContext())
                    .asBitmap()
                    .load(getAlbumArt(newPath))
                    .error(R.drawable.defaultimg)
                    .placeholder(R.drawable.defaultimg)
                    .into(image);
        }
    };



    private Handler handler = new Handler();
    private Runnable updateseekbar = new Runnable() {
        @Override
        public void run() {
            if(foregroundservice.mediaPlayer!=null && foregroundservice.mediaPlayer.isPlaying() ){
                int totalDuration = foregroundservice.mediaPlayer.getDuration();
                int currentPosition = foregroundservice.mediaPlayer.getCurrentPosition();

                if (seekBar.getMax() != totalDuration) { // default is 100, update once when real value is available
                    seekBar.setMax(totalDuration);
                }
                seekBar.setProgress(currentPosition);
                startingtime.setText(formatTime(currentPosition));
                endtime.setText(formatTime(totalDuration));
                handler.postDelayed(this,1000);
            }
        }
    };




    public playsonglist(){
        //empty
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        fav = new favoriteSong(getContext());
        View view = inflater.inflate(R.layout.fragment_playsonglist, container, false);
        title = view.findViewById(R.id.titlelist);
        artist = view.findViewById(R.id.artistlist);

        seekBar = view.findViewById(R.id.seekBar);

        startingtime = view.findViewById(R.id.starting);
        endtime = view.findViewById(R.id.ending);

        image = view.findViewById(R.id.imageView4);

        previous = view.findViewById(R.id.previousplay);
        play = view.findViewById(R.id.playplay);
        pause = view.findViewById(R.id.pauseplay);
        next = view.findViewById(R.id.nextplay);
        like = view.findViewById(R.id.like);
        dislike = view.findViewById(R.id.dislike);

        title.setText(singleton.getTitle());
        artist.setText(singleton.getArtist());
        updateImage();

        singleton.getInstance().getLiveTitle().observe(getViewLifecycleOwner(), newTitle -> {
            title.setText(newTitle);
        });

        singleton.getInstance().getLiveArtist().observe(getViewLifecycleOwner(), newArtist -> {
            artist.setText(newArtist);
        });

        singleton.getLivePath().observe(getViewLifecycleOwner(), newPath -> {
            // newPath is the new filePath — just reload the image
            updateImage();
        });


//        like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fav.insertFavorite(singleton.getTitle(),singleton.getArtist(), singleton.getSongpath());
//                dislike.setVisibility(View.VISIBLE);
//                like.setVisibility(View.INVISIBLE);
//            }
//        });
//
//        dislike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fav.deleteFavoriteByPath(singleton.getSongpath());
//                dislike.setVisibility(View.INVISIBLE);
//                like.setVisibility(View.VISIBLE);
//            }
//        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                message("start");
                handler.postDelayed(updateseekbar, 1000);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                message("pause");
                handler.removeCallbacks(updateseekbar);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                message("next");
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                message("previous");
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    foregroundservice.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireContext().registerReceiver(
                songChangedReceiver,
                new IntentFilter("com.example.dchord.SONG_CHANGED")
        );

        if (foregroundservice.mediaPlayer != null && foregroundservice.mediaPlayer.isPlaying()) {
            handler.post(updateseekbar);
        }

        if(singleton.isPlaying()){
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
        }
        else
        {
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);
        }

//        if (fav.isFavorite(singleton.getSongpath())) {
//            dislike.setVisibility(View.VISIBLE);
//            like.setVisibility(View.INVISIBLE);
//        }
//        else
//        {
//            dislike.setVisibility(View.INVISIBLE);
//            like.setVisibility(View.VISIBLE);
//        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_PLAY_PAUSE");
        filter.addAction("ACTION_PAUSE_PAUSE");
        requireContext().registerReceiver(playbackReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(songChangedReceiver);
        requireContext().unregisterReceiver(playbackReceiver);
        handler.removeCallbacks(updateseekbar);
    }

    private String formatTime(int timeInMillis) {
        int minutes = timeInMillis / 1000 / 60;
        int seconds = timeInMillis / 1000 % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void updateImage(){
        Glide.with(context)
                .asBitmap()
                .load(getAlbumArt(singleton.getSongpath()))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                .error(R.drawable.defaultimg)
                .placeholder(R.drawable.defaultimg)
                .into(image);
    }


    private byte[] getAlbumArt(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            byte[] art = retriever.getEmbeddedPicture();
            retriever.release();
            return art; // May be null, Glide handles it.
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void message(String action){
        Intent intent = new Intent(context, foregroundservice.class);
        intent.putExtra("action", action);
        context.startService(intent);
    }
}