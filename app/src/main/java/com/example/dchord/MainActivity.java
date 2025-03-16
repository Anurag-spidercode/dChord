package com.example.dchord;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.dchord.adapter;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private RecyclerView recyclerView;
    private adapter adaptr;

    private ImageButton playmain, pausemain;
    private ConstraintLayout layoutmain;
    private TextView titlemain;

    private String title;

    Intent intent;

    Viewmodel viewmodel;


    private final List<data> listmain = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewmodel = new ViewModelProvider(
                this, new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(Viewmodel.class);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Initialize views immediately after setContentView
        titlemain = findViewById(R.id.titlemain);
        playmain = findViewById(R.id.playmain);
        pausemain = findViewById(R.id.pausemain);
        layoutmain = findViewById(R.id.constraintLayout);

        // Initially hide views
        offocus();

        intent = new Intent(this, foregroundservice.class);

//        viewmodel.getsongName().observe(this, name ->{
//            if(name != null && !name.isEmpty()){
//                if(Title != null && Title.length() > 20)
//                {
//                    Title = Title.substring(0,20) + "...";
//                }
//                titlemain.setText(Title);
//                onfocus();
//            }
//            else
//            {
//                offocus();
//            }
//        });

        playmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playmain.setVisibility(View.GONE);
                pausemain.setVisibility(View.VISIBLE);
                intent.setAction("play");
                startService(intent);
            }
        });

        pausemain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausemain.setVisibility(View.GONE);
                playmain.setVisibility(View.VISIBLE);
                intent.setAction("pause");
                startService(intent);
            }
        });

        titlemain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(v.getContext(), Playsong.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }


    }

    private void onfocus() {
        titlemain.setVisibility(View.VISIBLE);
        playmain.setVisibility(View.VISIBLE);
        pausemain.setVisibility(View.VISIBLE);
        layoutmain.setVisibility(View.VISIBLE);

    }

    private  void offocus(){
        titlemain.setVisibility(View.GONE);
        playmain.setVisibility(View.GONE);
        pausemain.setVisibility(View.GONE);
        layoutmain.setVisibility(View.GONE);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            title = Singleton.getInstance().getTitle();
//
//            if (title != null && !title.isEmpty()) {
//                viewmodel.setSongname(title);
            viewmodel.getsongName().observe(this, title ->{
                if(title != null && !title.isEmpty()) {
                    onfocus();
                    boolean update = Singleton.getInstance().getUpdate();
                    String titler = "";
                    if (title.length() > 15) {
                        titler = title.substring(0, 15) + "...";
                        titlemain.setText(titler);
                    } else {
                        titlemain.setText(title);
                    }


                    if (update == true) {
                        playmain.setVisibility(View.GONE);
                        pausemain.setVisibility(View.VISIBLE);
//                    viewmodel.getsongName().observe(this, new Observer<String>() {
//                        @Override
//                        public void onChanged(String s) {
//                            if (s != null && !s.isEmpty()) {
//                                String displayTitle = s.length() > 15 ? s.substring(0, 15) + "..." : s;
//                                titlemain.setText(displayTitle);
//                                onfocus();
//                            } else {
//                                offocus();
//                            }
//                        }
//                    });
                    } else {
                        playmain.setVisibility(View.VISIBLE);
                        pausemain.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            //maybe in future. I said maybe if needed
        }
    }



    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // Android 13+ (API 33+)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, PERMISSION_REQUEST_CODE);
            } else {
                loadMusic();
            }
        } else { // Android 12 and below (API 23-32)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                loadMusic();
            }
        }
    }

    @SuppressLint("WrongViewCast")
//    private void loadMusic() {
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        list = MusicFetch.fetchAllSongs(getContentResolver());
//        adaptr = new adapter(list);
//        recyclerView.setAdapter(adaptr);
//        viewmodel.setSonglist(list);
//        viewmodel.getSongList().observe(this, songs ->{
//            if(songs != null) {
//                adaptr.updateSongs(songs);
//            }
//        });
//        Singleton.getInstance().setTitle(null);
//
//        TextView listsize = findViewById(R.id.listsize);
//        listsize.setText(list.size() + " Songs");
//    }
    private void loadMusic() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch songs
        List<data> songList = MusicFetch.fetchAllSongs(getContentResolver());

        if (songList != null && !songList.isEmpty()) {
            // Update ViewModel with non-null data
            viewmodel.setSonglist(songList);
        }

        // Observe ViewModel to update RecyclerView
        viewmodel.getSongList().observe(this, songs -> {
            if (songs != null && !songs.isEmpty()) {
                if (adaptr == null) {
                    adaptr = new adapter(songs, viewmodel);
                    recyclerView.setAdapter(adaptr);
                } else {
                    adaptr.updateSongs(songs);
                }
            }
        });



        // Update song count
        TextView listsize = findViewById(R.id.listsize);
        listsize.setText(songList.size() + " Songs");
    }


    @Override
    protected void onResume() {
        super.onResume();
        String currentTitle = viewmodel.getsongName().getValue();
        if (currentTitle != null && !currentTitle.isEmpty()) {
            String displayTitle = currentTitle.length() > 15 ? currentTitle.substring(0, 15) + "..." : currentTitle;
            titlemain.setText(displayTitle);
            onfocus();
        } else {
            offocus();
        }
    }






    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMusic();
            } else {
                Toast.makeText(this, "Permission Denied! Cannot access audio files.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
