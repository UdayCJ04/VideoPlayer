package com.example.openmediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AudioActivity extends AppCompatActivity {

    MediaPlayer player;
    Button playPause;

    boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        playPause = findViewById(R.id.btnPlayPause);

        String path = getIntent().getStringExtra("path");

        player = new MediaPlayer();

        try {
            player.setDataSource(path);
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        playPause.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
                playPause.setText("Play");
            } else {
                player.start();
                playPause.setText("Pause");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) player.release();
    }
}