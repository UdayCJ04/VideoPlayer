package com.example.openmediaplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String rootPath;
    String sortType = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // ✅ FIRST

        // 🔽 NOW views exist
        Spinner spinnerSort = findViewById(R.id.spinnerSort);

        String[] options = {"Name", "Size"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                options
        );

        spinnerSort.setAdapter(adapter);

        recyclerView = findViewById(R.id.videoRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                sortType = (position == 0) ? "name" : "size";
                loadFolders(rootPath);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        if (hasPermission()) {
            loadFolders(rootPath);
        } else {
            requestPermission();
        }
    }

    private void loadFolders(String path) {

        File dir = new File(path);
        File[] files = dir.listFiles();

        if (files == null) {
            Toast.makeText(this, "No access or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        List<FolderItem> list = new ArrayList<>();

        for (File file : files) {

            if (file.isDirectory() && containsMedia(file)) {
                list.add(new FolderItem(file.getName(), file.getAbsolutePath()));
            }
        }

        recyclerView.setAdapter(new FolderAdapter(this, list));
    }

    private boolean containsMedia(File dir) {

        File[] files = dir.listFiles();
        if (files == null) return false;

        for (File file : files) {

            if (file.isFile() && isMediaFile(file)) return true;

            if (file.isDirectory() && containsMedia(file)) return true;
        }

        return false;
    }

    private boolean isMediaFile(File file) {
        String name = file.getName().toLowerCase();

        return name.endsWith(".mp4") ||
                name.endsWith(".mkv") ||
                name.endsWith(".avi");
    }

    private boolean hasPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;

        } else {

            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_MEDIA_VIDEO,
                    },
                    100);

        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                loadFolders(rootPath);

            } else {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}