package com.example.openmediaplayer;

public class MediaItem {

    private String name;
    private String path;
    private String type;

    public MediaItem(String name, String path, String type) {
        this.name = name;
        this.path = path;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }
}