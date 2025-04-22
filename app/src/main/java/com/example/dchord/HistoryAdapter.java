package com.example.dchord;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.SongViewHolder>{

    public static final String filepathurl = "com.example.dchord.playmusic";
    public static final String filepathurl2 = "com.example.dchord.playmusic2";
    public static final String filepathurl3 = "com.example.dchord.playmusic3";
    private List<HistorySong> songList;

    private String path = "";
    private String title = "";
    private String artist = "";

    private Context context;

    int index = 0;
    Singleton singleton = Singleton.getInstance();

    public HistoryAdapter(List<HistorySong> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        PlaybackSongs history = new PlaybackSongs(context);
        HistorySong song = songList.get(position);
        path = song.getPath();
        String titleDummy = song.getTitle();
        String artistDummy = song.getArtist();
        names(titleDummy,artistDummy);
        holder.titleTextView.setText(title);
        holder.artistTextView.setText(artist);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(song == null){
                    return;
                }

//                viewmodel.setCurrentIndex(pos);
                history.insertSong(song.getTitle(), song.getArtist(), song.getPath());//inserting to database;
//                singleton.setSongList(songList);
                index = holder.getAdapterPosition();
                singleton.setPosition(index);
                Intent intent = new Intent(v.getContext(), foregroundservice.class);
                intent.putExtra(filepathurl, song.getTitle());
                intent.putExtra(filepathurl2, song.getArtist());
                intent.putExtra(filepathurl3, song.getPath());
//                intent.setAction("play");
                ContextCompat.startForegroundService(v.getContext(), intent);

                if (v.getContext() instanceof MainActivity) {
                    MainActivity swap = (MainActivity) v.getContext();
                    swap.fragmentswap(new playsonglist());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    private void names(String titleDummy, String artistDummy){
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
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView artistTextView;

        ConstraintLayout constraintLayout;

        public SongViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titlename);
            artistTextView = itemView.findViewById(R.id.artist);
            constraintLayout = itemView.findViewById(R.id.layout1);
        }
    }
}
