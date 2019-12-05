package com.free.top.tvshows.tube.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.activities.TVShowActivity;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.free.top.tvshows.tube.listeners.ListInteractionsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.ViewHolder> {

    private List<TVShow> mDataSet;
    private ListInteractionsListener listInteractionsListener;
    private boolean horizontal = false;

    public TVShowsAdapter(List<TVShow> data) {
        mDataSet = data;
    }

    public TVShowsAdapter(List<TVShow> data, boolean horizontal) {
        mDataSet = data;
        this.horizontal = horizontal;
    }

    public TVShowsAdapter(List<TVShow> data, ListInteractionsListener listInteractionsListener) {
        mDataSet = data;
        this.listInteractionsListener = listInteractionsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (horizontal) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_horizontal_list_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TVShow tvShow = mDataSet.get(position);
        Picasso.with(holder.poster.getContext())
                .load("http://image.tmdb.org/t/p/w342" + tvShow.getPosterPath())
                .placeholder(R.drawable.no_poster)
                .error(R.drawable.no_poster)
                .into(holder.poster);

        holder.title.setText(tvShow.getName());
        if (!tvShow.getFirstAirDate().isEmpty())
            holder.releaseDate.setText(tvShow.getFirstAirDate().substring(0, 4));
        else
            holder.releaseDate.setText("--");
        if (!horizontal) {
            StringBuilder genresStr = new StringBuilder();
            for (int i = 0; i < tvShow.getGenres().length; i++) {
                if (tvShow.getGenres()[i] != null) {
                    if (i != tvShow.getGenres().length - 1)
                        genresStr.append(tvShow.getGenres()[i].getName()).append(", ");
                    else
                        genresStr.append(tvShow.getGenres()[i].getName());
                } else
                    Log.e("TVShowsAdapter", tvShow.getName() + "\n" + tvShow.getId() + "\n" + "genre " + i + " of " +tvShow.getGenres().length);
            }
            holder.genres.setText(genresStr.toString());
        }

        if (listInteractionsListener != null && position > mDataSet.size() - 10)
            listInteractionsListener.onListEndReached();

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView poster;
        private TextView title;
        private TextView releaseDate;
        private TextView genres;

        public ViewHolder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            releaseDate = itemView.findViewById(R.id.release_date);
            if (!horizontal) {
                genres = itemView.findViewById(R.id.genres);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), TVShowActivity.class);
            intent.putExtra("tv_show", mDataSet.get(getAdapterPosition()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                View poster = view.findViewById(R.id.poster);
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), poster, "poster_transition");
                view.getContext().startActivity(intent, activityOptions.toBundle());
            } else {
                view.getContext().startActivity(intent);
            }
        }
    }
}
