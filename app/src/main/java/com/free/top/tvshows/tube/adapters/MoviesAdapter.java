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
import com.free.top.tvshows.tube.activities.MovieActivity;
import com.free.top.tvshows.tube.api.model.Movie;
import com.free.top.tvshows.tube.listeners.ListInteractionsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;

    private int orientation;

    private List<Movie> mDataSet;
    private ListInteractionsListener listInteractionsListener;

    public MoviesAdapter(List<Movie> data) {
        mDataSet = data;
    }

    public MoviesAdapter(List<Movie> data, ListInteractionsListener listInteractionsListener) {
        mDataSet = data;
        this.listInteractionsListener = listInteractionsListener;
    }

    public MoviesAdapter(List<Movie> data, int orientation) {
        mDataSet = data;
        this.orientation = orientation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (orientation == HORIZONTAL) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_horizontal_list_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mDataSet.get(position);
        Picasso.with(holder.poster.getContext())
                .load("http://image.tmdb.org/t/p/w342" + movie.getPosterPath())
                .placeholder(R.drawable.no_poster)
                .error(R.drawable.no_poster)
                .into(holder.poster);

        holder.title.setText(movie.getTitle());
        if (movie.getReleaseDate() != null && movie.getReleaseDate().length() >=4)
            holder.releaseDate.setText(movie.getReleaseDate().substring(0, 4));
        else
            holder.releaseDate.setText(null);
        if (orientation == VERTICAL) {
            StringBuilder genresStr = new StringBuilder();
            if (movie.getGenres() != null) {
                for (int i = 0; i < movie.getGenres().length; i++) {
                    if (movie.getGenres()[i] != null) {
                        if (i != movie.getGenres().length - 1)
                            genresStr.append(movie.getGenres()[i].getName()).append(", ");
                        else
                            genresStr.append(movie.getGenres()[i].getName());
                    }
                }
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
            if (orientation == VERTICAL) {
                genres = itemView.findViewById(R.id.genres);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MovieActivity.class);
            intent.putExtra("movie", mDataSet.get(getAdapterPosition()));

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
