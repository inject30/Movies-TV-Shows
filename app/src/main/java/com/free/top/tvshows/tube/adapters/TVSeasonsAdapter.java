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
import com.free.top.tvshows.tube.activities.TVSeasonActivity;
import com.free.top.tvshows.tube.api.model.TVSeason;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.squareup.picasso.Picasso;

public class TVSeasonsAdapter extends RecyclerView.Adapter<TVSeasonsAdapter.ViewHolder> {

    private TVShow mTVShow;

    public TVSeasonsAdapter(TVShow tvShow) {
        mTVShow = tvShow;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TVSeason season = mTVShow.getSeasons()[position];
            Picasso.with(holder.poster.getContext())
                    .load("http://image.tmdb.org/t/p/w342" + season.getPosterPath())
                    .placeholder(R.drawable.no_poster)
                    .error(R.drawable.no_poster)
                    .into(holder.poster);

        holder.seasonNumber.setText(season.getSeasonNumber() + " " + holder.seasonNumber.getContext().getString(R.string.season));
        holder.airDate.setText(season.getAirDate().substring(0, 4));
        holder.episodeCount.setText(season.getEpisodeCount() + " " + holder.episodeCount.getContext().getString(R.string.episodes));
    }

    @Override
    public int getItemCount() {
        return mTVShow.getSeasons().length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView airDate;
        private TextView episodeCount;
        private TextView seasonNumber;
        private ImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);

            airDate = itemView.findViewById(R.id.air_date);
            episodeCount = itemView.findViewById(R.id.episode_count);
            seasonNumber = itemView.findViewById(R.id.season_number);
            poster = itemView.findViewById(R.id.poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), TVSeasonActivity.class);
            intent.putExtra("tv_show", mTVShow);
            intent.putExtra("tv_season", mTVShow.getSeasons()[getAdapterPosition()]);

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
