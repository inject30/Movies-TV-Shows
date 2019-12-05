package com.free.top.tvshows.tube.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.activities.PersonActivity;
import com.free.top.tvshows.tube.api.model.Cast;
import com.free.top.tvshows.tube.api.model.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CastsAdapter extends RecyclerView.Adapter<CastsAdapter.ViewHolder> {

    private ArrayList<Cast> mDataSet;

    public CastsAdapter(ArrayList<Cast> casts) {
        mDataSet = casts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.casts_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cast cast = mDataSet.get(position);
        Picasso.with(holder.profile.getContext())
                .load("http://image.tmdb.org/t/p/w185" + cast.getProfilePath())
                .placeholder(R.drawable.no_profile)
                .placeholder(R.drawable.no_profile)
                .into(holder.profile);

        holder.name.setText(cast.getName());
        holder.character.setText(holder.character.getContext().getString(R.string.as_character) + " " + cast.getCharacter());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView profile;
        private TextView name;
        private TextView character;

        public ViewHolder(View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            character = itemView.findViewById(R.id.character);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PersonActivity.class);
            Person person = new Person.Builder().withCast(mDataSet.get(getAdapterPosition())).build();
            intent.putExtra("person", person);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                View profile = view.findViewById(R.id.profile);
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), profile, "profile_transition");
                view.getContext().startActivity(intent, activityOptions.toBundle());
            } else {
                view.getContext().startActivity(intent);
            }
        }
    }
}
