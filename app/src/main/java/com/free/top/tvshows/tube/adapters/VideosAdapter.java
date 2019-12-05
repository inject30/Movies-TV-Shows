package com.free.top.tvshows.tube.adapters;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.api.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private List<Video> mDataSet;

    public VideosAdapter(List<Video> videos) {
        mDataSet = videos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = mDataSet.get(position);
        Picasso.with(holder.thumbnail.getContext())
                .load("https://img.youtube.com/vi/" + video.getKey() + "/hqdefault.jpg")
                .into(holder.thumbnail);

        holder.name.setText(video.getName());
        if (video.getSize() >= 720)
            holder.hd.setVisibility(View.VISIBLE);
        else
            holder.hd.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumbnail;
        private ImageView hd;
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.name);
            hd = itemView.findViewById(R.id.hd);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mDataSet.get(getAdapterPosition()).getKey()));
            view.getContext().startActivity(intent);
        }
    }
}
