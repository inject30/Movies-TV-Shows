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
import com.free.top.tvshows.tube.activities.TVShowActivity;
import com.free.top.tvshows.tube.api.model.TVCastCredit;
import com.free.top.tvshows.tube.api.model.TVCrewCredit;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TVCreditsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TVCastCredit> mCast;
    private ArrayList<TVCrewCredit> mCrew;

    public TVCreditsAdapter(ArrayList<TVCastCredit> tvCast, ArrayList<TVCrewCredit> tvCrew) {
        mCast = tvCast;
        mCrew = tvCrew;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mCast.size() + 1) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new DividerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.credits_divider, parent, false));
        }
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_credit_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                DividerViewHolder dividerViewHolder = (DividerViewHolder) holder;
                if (position == 0) {
                    dividerViewHolder.label.setText("Acting");
                    dividerViewHolder.count.setText(dividerViewHolder.count.getContext()
                            .getResources()
                            .getQuantityString(R.plurals.credits, mCast.size(), mCast.size()));
                } else {
                    dividerViewHolder.label.setText("Production");
                    dividerViewHolder.count.setText(dividerViewHolder.count.getContext()
                            .getResources()
                            .getQuantityString(R.plurals.credits, mCrew.size(), mCrew.size()));
                }
                break;
            case 1:
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

                if (position <= mCast.size()) {
                    TVCastCredit castCredit = mCast.get(position - 1);

                    Picasso.with(itemViewHolder.poster.getContext())
                            .load("http://image.tmdb.org/t/p/w342" + castCredit.getPosterPath())
                            .placeholder(R.drawable.no_poster)
                            .error(R.drawable.no_poster)
                            .into(itemViewHolder.poster);

                    itemViewHolder.title.setText(castCredit.getName());
                    if (castCredit.getFirstAirDate() != null && castCredit.getFirstAirDate().length() >=4)
                        itemViewHolder.releaseDate.setText(castCredit.getFirstAirDate().substring(0, 4));
                    else
                        itemViewHolder.releaseDate.setText(castCredit.getFirstAirDate());
                    if (castCredit.getCharacter() != null && !castCredit.getCharacter().isEmpty())
                        itemViewHolder.character.setText(itemViewHolder.character.getContext().getString(R.string.as_character) + " " + castCredit.getCharacter());
                    else
                        itemViewHolder.character.setText(castCredit.getCharacter());
                } else {
                    TVCrewCredit crewCredit = mCrew.get(position - mCast.size() - 2);

                    Picasso.with(itemViewHolder.poster.getContext())
                            .load("http://image.tmdb.org/t/p/w342" + crewCredit.getPosterPath())
                            .placeholder(R.drawable.no_poster)
                            .error(R.drawable.no_poster)
                            .into(itemViewHolder.poster);

                    itemViewHolder.title.setText(crewCredit.getName());
                    if (crewCredit.getFirstAirDate() != null && crewCredit.getFirstAirDate().length() >= 4)
                        itemViewHolder.releaseDate.setText(crewCredit.getFirstAirDate().substring(0, 4));
                    else
                        itemViewHolder.releaseDate.setText(crewCredit.getFirstAirDate());
                    itemViewHolder.character.setText(crewCredit.getJob());
                }
        }
    }

    @Override
    public int getItemCount() {
        if (!mCrew.isEmpty())
            return mCast.size() + mCrew.size() + 2;
        else
            return mCast.size() + 1;
    }

    public class DividerViewHolder extends RecyclerView.ViewHolder {

        private TextView label;
        private TextView count;


        DividerViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            count = itemView.findViewById(R.id.count);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
                Intent intent = new Intent(view.getContext(), TVShowActivity.class);
                intent.putExtra("tv_show", new TVShow.Builder().withTVCastCredit(mCast.get(getAdapterPosition() - 1)).build());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    View poster = view.findViewById(R.id.poster);
                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), poster, "poster_transition");
                    view.getContext().startActivity(intent, activityOptions.toBundle());
                } else {
                    view.getContext().startActivity(intent);
                }
            } else if (getAdapterPosition() > mCast.size() + 1) {
                Intent intent = new Intent(view.getContext(), TVShowActivity.class);
                intent.putExtra("tv_show", new TVShow.Builder().withTVCrewCredit(mCrew.get(getAdapterPosition() - mCast.size() - 2)).build());

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
