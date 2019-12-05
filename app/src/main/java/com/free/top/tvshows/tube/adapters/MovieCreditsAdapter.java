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
import com.free.top.tvshows.tube.api.model.CastCredit;
import com.free.top.tvshows.tube.api.model.CrewCredit;
import com.free.top.tvshows.tube.api.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieCreditsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CastCredit> mCast;
    private ArrayList<CrewCredit> mCrew;

    public MovieCreditsAdapter(ArrayList<CastCredit> cast, ArrayList<CrewCredit> crew) {
        mCast = cast;
        mCrew = crew;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position == 0 || position == mCast.size() + 1)) {
            return 1;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_credit_list_item, parent, false));
        }
        return new DividerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.credits_divider, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

                if (position <= mCast.size()) {
                    CastCredit castCredit = mCast.get(position - 1);

                    Picasso.with(itemViewHolder.poster.getContext())
                            .load("http://image.tmdb.org/t/p/w342" + castCredit.getPosterPath())
                            .placeholder(R.drawable.no_poster)
                            .error(R.drawable.no_poster)
                            .into(itemViewHolder.poster);

                    itemViewHolder.title.setText(castCredit.getTitle());
                    if (castCredit.getReleaseDate() != null && castCredit.getReleaseDate().length() >=4)
                        itemViewHolder.releaseDate.setText(castCredit.getReleaseDate().substring(0, 4));
                    else
                        itemViewHolder.releaseDate.setText(castCredit.getReleaseDate());
                    if (castCredit.getCharacter() != null && !castCredit.getCharacter().isEmpty())
                        itemViewHolder.character.setText(itemViewHolder.character.getContext().getString(R.string.as_character) + " " + castCredit.getCharacter());
                    else
                        itemViewHolder.character.setText(castCredit.getCharacter());
                } else {
                    CrewCredit crewCredit = mCrew.get(position - mCast.size() - 2);

                    Picasso.with(itemViewHolder.poster.getContext())
                            .load("http://image.tmdb.org/t/p/w342" + crewCredit.getPosterPath())
                            .placeholder(R.drawable.no_poster)
                            .error(R.drawable.no_poster)
                            .into(itemViewHolder.poster);

                    itemViewHolder.title.setText(crewCredit.getTitle());
                    if (crewCredit.getReleaseDate() != null && crewCredit.getReleaseDate().length() >=4)
                        itemViewHolder.releaseDate.setText(crewCredit.getReleaseDate().substring(0, 4));
                    else
                        itemViewHolder.releaseDate.setText(crewCredit.getReleaseDate());
                    itemViewHolder.character.setText(crewCredit.getJob());
                }
                break;
            case 1:
                DividerViewHolder dividerViewHolder = (DividerViewHolder) holder;
                if (position == 0) {
                    dividerViewHolder.label.setText("Acting");
                    dividerViewHolder.count.setText(dividerViewHolder.count.getContext()
                            .getResources()
                            .getQuantityString(R.plurals.credits, mCast.size(), mCast.size()));
                } else {
                    dividerViewHolder.label.setText("Crew");
                    dividerViewHolder.count.setText(dividerViewHolder.count.getContext()
                            .getResources()
                            .getQuantityString(R.plurals.credits, mCrew.size(), mCrew.size()));
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (!mCrew.isEmpty())
            return mCast.size() + mCrew.size() + 2;
        else
            return mCast.size() + 1;
    }

    private class DividerViewHolder extends RecyclerView.ViewHolder {

        private TextView label;
        private TextView count;


        DividerViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            count = itemView.findViewById(R.id.count);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView poster;
        private TextView title;
        private TextView releaseDate;
        private TextView character;

        ItemViewHolder(View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            releaseDate = itemView.findViewById(R.id.release_date);
            character = itemView.findViewById(R.id.character);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() < mCast.size() + 1) {
                Intent intent = new Intent(view.getContext(), MovieActivity.class);
                intent.putExtra("movie", new Movie.Builder().withCastCredit(mCast.get(getAdapterPosition() - 1)).build());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    View poster = view.findViewById(R.id.poster);
                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), poster, "poster_transition");
                    view.getContext().startActivity(intent, activityOptions.toBundle());
                } else {
                    view.getContext().startActivity(intent);
                }
            } else if (getAdapterPosition() > mCast.size() + 1) {
                Intent intent = new Intent(view.getContext(), MovieActivity.class);
                intent.putExtra("movie", new Movie.Builder().withCrewCredit(mCrew.get(getAdapterPosition() - mCast.size() - 2)).build());
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
}
