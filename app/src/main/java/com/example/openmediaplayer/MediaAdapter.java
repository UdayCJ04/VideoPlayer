package com.example.openmediaplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UnstableApi
public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {

    Context context;
    List<MediaItem> list;



    public MediaAdapter(Context context, List<MediaItem> list) {
        this.context = context;
        this.list = list;
    }

    // ✅ Thumbnail method


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MediaItem item = list.get(position);

        holder.title.setText(item.getName());

        String path = item.getPath();
        String type = item.getType();

        if ("video".equals(type)) {

            Glide.with(context)
                    .load(new File(path))
                    .placeholder(R.drawable.ic_video)
                    .centerCrop()
                    .into(holder.icon);

        } else if ("audio".equals(type)) {

            holder.icon.setImageResource(R.drawable.ic_audio);

        } else if ("image".equals(type)) {

            Glide.with(context)
                    .load(new File(path))
                    .placeholder(R.drawable.ic_file)
                    .centerCrop()
                    .into(holder.icon);
        }

        // ▶️ Click handling
        holder.itemView.setOnClickListener(v -> {

            Intent intent;

            switch (type) {

                case "video":
                    intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("videoPath", path);
                    break;

                default:
                    return;
            }

            context.startActivity(intent);
        });

        Glide.with(context)
                .load(new File(path))
                .placeholder(R.drawable.ic_video)
                .centerCrop()
                .into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ✅ FIXED ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.videoIcon);
            title = itemView.findViewById(R.id.videoTitle);
        }
    }
}