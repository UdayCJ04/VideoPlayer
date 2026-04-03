package com.example.openmediaplayer;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

@UnstableApi
public class PlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;

    private long playbackPosition = 0;
    private boolean playWhenReady = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong("position");
            playWhenReady = savedInstanceState.getBoolean("playWhenReady");
        }

        playerView = findViewById(R.id.playerView);
        playerView.setShowSubtitleButton(true);
        playerView.setUseController(true);

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        String path = getIntent().getStringExtra("videoPath");

        if (path != null) {
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(path));
            player.setMediaItem(mediaItem);
            player.prepare();

            player.seekTo(playbackPosition);
            player.setPlayWhenReady(playWhenReady);
        }

        // FULLSCREEN FIX
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());

        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            playWhenReady = player.getPlayWhenReady();
            player.pause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("position", playbackPosition);
        outState.putBoolean("playWhenReady", playWhenReady);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}