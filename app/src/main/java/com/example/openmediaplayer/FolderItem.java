package com.example.openmediaplayer;

public class FolderItem {

    private String folderName;
    private String folderPath;

    public FolderItem(String folderName, String folderPath) {
        this.folderName = folderName;
        this.folderPath = folderPath;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }
}