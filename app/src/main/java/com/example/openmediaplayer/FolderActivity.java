package com.example.openmediaplayer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@UnstableApi
public class FolderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txtPath;

    private String currentPath;
    private String sortType = "name"; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.videoRecycler);
        txtPath = findViewById(R.id.txtPath);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentPath = getIntent().getStringExtra("folderPath");

        if (currentPath == null) {
            finish();
            return;
        }

        Button sortBtn = findViewById(R.id.btnSort);

        if (sortBtn != null) {
            sortBtn.setOnClickListener(v -> {

                if (sortType.equals("name")) {
                    sortType = "size";
                    Toast.makeText(this, "Sorted by Size", Toast.LENGTH_SHORT).show();
                } else {
                    sortType = "name";
                    Toast.makeText(this, "Sorted by Name", Toast.LENGTH_SHORT).show();
                }

                loadContent(currentPath);
            });
        }

        updatePathUI(currentPath);
        loadContent(currentPath);
    }

    // 📍 Show clean path
    private void updatePathUI(String fullPath) {
        String root = "/storage/emulated/0/";
        String displayPath = fullPath.replace(root, "Internal/");
        txtPath.setText(displayPath);
    }

    // 🔥 MAIN LOGIC
    private void loadContent(String path) {

        File dir = new File(path);
        File[] files = dir.listFiles();

        if (files == null) return;

        List<File> folders = new ArrayList<>();
        List<File> mediaFiles = new ArrayList<>();

        for (File file : files) {

            if (file.isHidden()) continue;

            if (file.isDirectory()) {
                folders.add(file);
            } else {

                String name = file.getName().toLowerCase();

                if (name.endsWith(".mp4") ||
                        name.endsWith(".mkv") ||
                        name.endsWith(".avi")) {

                    mediaFiles.add(file);
                }
            }
        }

        // 📂 PRIORITY: folders first
        if (!folders.isEmpty()) {

            List<FolderItem> folderItems = new ArrayList<>();

            for (File file : folders) {
                folderItems.add(new FolderItem(
                        file.getName(),
                        file.getAbsolutePath()
                ));
            }

            recyclerView.setAdapter(new FolderAdapter(this, folderItems));
        }

        // 🎬 MEDIA
        else {

            sortFiles(mediaFiles);

            List<MediaItem> mediaItems = new ArrayList<>();

            for (File file : mediaFiles) {

                String name = file.getName().toLowerCase();
                String type = "video";

                if (name.endsWith(".mp3") || name.endsWith(".wav")) {
                    type = "audio";
                } else if (name.endsWith(".jpg") || name.endsWith(".png")) {
                    type = "image";
                }

                mediaItems.add(new MediaItem(
                        file.getName(),
                        file.getAbsolutePath(),
                        type
                ));
            }

            recyclerView.setAdapter(new MediaAdapter(this, mediaItems));
        }
    }

    // 🔄 SORTING
    private void sortFiles(List<File> files) {

        files.sort((f1, f2) -> {

            if (sortType.equals("name")) {
                return f1.getName().compareToIgnoreCase(f2.getName());
            } else {
                return Long.compare(f2.length(), f1.length());
            }
        });
    }
}