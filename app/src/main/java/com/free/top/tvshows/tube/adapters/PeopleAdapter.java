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
import com.free.top.tvshows.tube.api.model.Movie;
import com.free.top.tvshows.tube.api.model.Person;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.free.top.tvshows.tube.listeners.ListInteractionsListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private List<Person> mDataSet;

    private ListInteractionsListener listInteractionsListener;

    public PeopleAdapter(List<Person> people, ListInteractionsListener listInteractionsListener) {
        mDataSet = people;
        this.listInteractionsListener = listInteractionsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = mDataSet.get(position);

        Picasso.with(holder.profile.getContext())
                .load("http://image.tmdb.org/t/p/w185" + person.getProfilePath())
                .placeholder(R.drawable.no_profile)
                .error(R.drawable.no_profile)
                .into(holder.profile);


        holder.name.setText(person.getName());
        if (person.getKnownFor() != null) {
            StringBuilder knownForStr = new StringBuilder();
            for (int i = 0; i < person.getKnownFor().length; i++) {
                if (person.getKnownFor()[i] instanceof Movie) {
                    Movie movie = (Movie) person.getKnownFor()[i];
                    if (i == person.getKnownFor().length - 1)
                        knownForStr.append(movie.getTitle());
                    else
                        knownForStr.append(movie.getTitle()).append(", ");
                } else {
                    TVShow tvShow = (TVShow) person.getKnownFor()[i];
                    if (i == person.getKnownFor().length - 1)
                        knownForStr.append(tvShow.getName());
                    else
                        knownForStr.append(tvShow.getName()).append(", ");
                }

            }
            holder.knownFor.setText(knownForStr.toString());
        } else {
            holder.knownFor.setText(null);
        }

        if (listInteractionsListener != null && position > mDataSet.size() - 10)
            listInteractionsListener.onListEndReached();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView profile;
        private TextView name;
        private TextView knownFor;

        public ViewHolder(View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            knownFor = itemView.findViewById(R.id.known_for);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PersonActivity.class);
            intent.putExtra("person", mDataSet.get(getAdapterPosition()));
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
