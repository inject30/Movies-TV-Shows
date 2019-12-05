package com.free.top.tvshows.tube.adapters;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.activities.ImageGalleryActivity;
import com.free.top.tvshows.tube.api.model.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private Person mPerson;
    private ArrayList<String> mDataSet;

    public ImagesAdapter(Person person, ArrayList<String> data) {
        mPerson = person;
        mDataSet = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(holder.image.getContext())
                .load("http://image.tmdb.org/t/p/w185" + mDataSet.get(position))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ImageGalleryActivity.class);
            intent.putStringArrayListExtra("images", mDataSet);
            intent.putExtra("position", getAdapterPosition());
            intent.putExtra("person", mPerson);
            view.getContext().startActivity(intent);
        }
    }
}
