package com.example.openmediaplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private Context context;
    private List<FolderItem> folderList;


    public FolderAdapter(Context context, List<FolderItem> folderList) {
        this.context = context;
        this.folderList = folderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.folder_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FolderItem folder = folderList.get(position);

        holder.folderName.setText(folder.getFolderName());


        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, FolderActivity.class);
            intent.putExtra("folderPath", folder.getFolderPath());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    // 🔹 ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView folderName;

        public ViewHolder(View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
        }
    }
}
