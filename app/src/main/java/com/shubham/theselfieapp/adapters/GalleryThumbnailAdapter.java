package com.shubham.theselfieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shubham.theselfieapp.Image;
import com.shubham.theselfieapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Shubham Gupta on 01-09-2016.
 */
public class GalleryThumbnailAdapter extends RecyclerView.Adapter<GalleryThumbnailAdapter.MenuHolder> {
 public ArrayList<Image> filePath;
 Context context;
    @Override
    public GalleryThumbnailAdapter.MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        context = parent.getContext();
        v  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item, parent, false);
        MenuHolder menuHolder = new MenuHolder(v);
        return menuHolder;
    }

    @Override
    public void onBindViewHolder(GalleryThumbnailAdapter.MenuHolder holder, int position) {
        Uri uri = Uri.fromFile(new File(filePath.get(position).getFileName()));
        if(filePath.get(position).getFileName().endsWith("mp4")){
            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(filePath.get(position).getFileName(), MediaStore.Video.Thumbnails.MICRO_KIND);
            holder.thumbnailImageView.setImageBitmap(bMap);
            holder.playButtonImageView.setVisibility(View.VISIBLE);
        }else {
            Picasso.with(context).load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(96, 96)
                    .into(holder.thumbnailImageView);
            Log.e("TAG", "path:" + filePath.get(position));
            holder.playButtonImageView.setVisibility(View.GONE);
        }

        if(filePath.get(position).getSyncStatus().equals(Image.SYNC_STATUS.NOT_SYNCED)){
            holder.syncStatusImageView.setVisibility(View.GONE);
        }
        else
            holder.syncStatusImageView.setVisibility(View.VISIBLE);

        int pos = filePath.get(position).getFileName().lastIndexOf("/");
        holder.imageName.setText(filePath.get(position).getFileName().substring(pos+1));
    }

    @Override
    public int getItemCount() {
        return filePath.size();
    }
    public class MenuHolder extends RecyclerView.ViewHolder {
     ImageView thumbnailImageView;
        ImageView playButtonImageView;
        TextView imageName;
        ImageView syncStatusImageView;

        public MenuHolder(View itemView) {
            super(itemView);
          thumbnailImageView = (ImageView)itemView.findViewById(R.id.thumbnail);
            playButtonImageView = (ImageView)itemView.findViewById(R.id.playButton);
            syncStatusImageView = (ImageView)itemView.findViewById(R.id.syncStatusImageView);
            imageName = (TextView)itemView.findViewById(R.id.imageName);
            thumbnailImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(filePath.get(getAdapterPosition()).getFileName()),"image/* video/*");
                    context.startActivity(intent);
                }
            });

        }
    }
}
