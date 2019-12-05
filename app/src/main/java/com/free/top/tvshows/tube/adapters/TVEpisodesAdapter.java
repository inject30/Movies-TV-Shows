package com.free.top.tvshows.tube.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.api.model.TVEpisode;
import com.free.top.tvshows.tube.api.model.TVSeason;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class TVEpisodesAdapter extends RecyclerView.Adapter<TVEpisodesAdapter.ViewHolder> {

    private TVSeason mTVSeason;

    public TVEpisodesAdapter(TVSeason tvSeason) {
        mTVSeason = tvSeason;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TVEpisode episode = mTVSeason.getEpisodes()[position];


        Picasso.with(holder.still.getContext())
                .load("http://image.tmdb.org/t/p/w300" + episode.getStillPath())
                .placeholder(R.drawable.no_still)
                .error(R.drawable.no_still)
                .into(holder.still);

        holder.episodeNumber.setText(episode.getEpisodeNumber() + " " + holder.episodeNumber.getContext().getString(R.string.episode));
        holder.name.setText(episode.getName());
        if (episode.getOverview() != null && !episode.getOverview().isEmpty()) {
            holder.overview.setVisibility(View.VISIBLE);
            holder.overview.setText(episode.getOverview());
        } else
            holder.overview.setVisibility(View.GONE);

        try {
            DateTime airDate = DateTimeFormat.forPattern("yyyy-mm-dd").parseDateTime(episode.getAirDate());
            holder.airDate.setText(DateTimeFormat.longDate().print(airDate.toLocalDate()));
        } catch (IllegalArgumentException|NullPointerException e) {
            holder.airDate.setText(episode.getAirDate());
        }
    }

    @Override
    public int getItemCount() {
        return mTVSeason.getEpisodes().length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView still;
        private TextView airDate;
        private TextView name;
        private TextView episodeNumber;
        private TextView overview;

        public ViewHolder(View itemView) {
            super(itemView);

            still = itemView.findViewById(R.id.still);
            airDate = itemView.findViewById(R.id.air_date);
            name = itemView.findViewById(R.id.name);
            episodeNumber = itemView.findViewById(R.id.episode_number);
            overview = itemView.findViewById(R.id.overview);
        }
    }
}
