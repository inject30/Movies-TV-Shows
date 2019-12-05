package com.free.top.tvshows.tube.fragments.people;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.activities.MovieActivity;
import com.free.top.tvshows.tube.activities.TVShowActivity;
import com.free.top.tvshows.tube.adapters.ImagesAdapter;
import com.free.top.tvshows.tube.api.model.Movie;
import com.free.top.tvshows.tube.api.model.Person;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.free.top.tvshows.tube.utils.HorizontalSpaceItemDecoration;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

public class InfoTab extends Fragment {

    private Person mPerson;
    private ArrayList<String> mImages;

    private TextView mBiography;
    private TextView mGender;
    private TextView mBirthday;
    private TextView mDeathday;
    private View mDeathdayHolder;
    private TextView mOfficialSite;
    private TextView mPlaceOfBirth;
    private TextView mAlsoKnownAs;
    private RecyclerView mImagesRecyclerView;
    private RecyclerView mKnownForRecyclerView;

    private View mImagesContainer;
    private View mKnownForContainer;

    private BroadcastReceiver mPersonDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPerson = (Person) intent.getSerializableExtra("person");
            mImages = (ArrayList<String>) intent.getSerializableExtra("images");

            if (mPerson.getBiography() != null && !mPerson.getBiography().isEmpty())
                mBiography.setText(mPerson.getBiography());
            else
                mBiography.setText(getString(R.string.no_biography) + " " + mPerson.getName());

            switch (mPerson.getGender()) {
                case 1:
                    mGender.setText(R.string.female);
                    break;
                case 2:
                    mGender.setText(R.string.male);
                    break;
                default:
                    mGender.setText(R.string.unknown);
                    break;
            }

            if (mPerson.getBirthday() != null && !mPerson.getBirthday().isEmpty()) {
                DateTime bDate = DateTimeFormat.forPattern("yyyy-mm-dd").parseDateTime(mPerson.getBirthday());
                mBirthday.setText(DateTimeFormat.longDate().print(bDate.toLocalDate()));
            }
            if (mPerson.getDeathday() != null && !mPerson.getDeathday().isEmpty()) {
                mDeathdayHolder.setVisibility(View.VISIBLE);
                DateTime dDate = DateTimeFormat.forPattern("yyyy-mm-dd").parseDateTime(mPerson.getDeathday());
                mDeathday.setText(DateTimeFormat.longDate().print(dDate.toLocalDate()));
            } else {
                mDeathdayHolder.setVisibility(View.GONE);
            }
            mOfficialSite.setText(mPerson.getHomepage());
            mPlaceOfBirth.setText(mPerson.getPlaceOfBirth());

            StringBuilder alsoKnownAsStr = new StringBuilder();
            for (int i = 0; i < mPerson.getAlsoKnownAs().length; i++) {
                if (i < mPerson.getAlsoKnownAs().length - 1) {
                    alsoKnownAsStr.append(mPerson.getAlsoKnownAs()[i]).append(", ");
                } else {
                    alsoKnownAsStr.append(mPerson.getAlsoKnownAs()[i]);
                }
            }
            if (alsoKnownAsStr.length() > 0)
                mAlsoKnownAs.setText(alsoKnownAsStr.toString());

            if (mImages != null && !mImages.isEmpty()) {
                mImagesContainer.setVisibility(View.VISIBLE);
                mImagesRecyclerView.setAdapter(new ImagesAdapter(mPerson, mImages));
            } else
                mImagesContainer.setVisibility(View.GONE);

            if (mPerson.getKnownFor() != null && mPerson.getKnownFor().length > 0) {
                mKnownForContainer.setVisibility(View.VISIBLE);
                mKnownForRecyclerView.setAdapter(new KnownForAdapter());
            } else
                mKnownForContainer.setVisibility(View.GONE);

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPersonDetailsReceiver);
            mPersonDetailsReceiver = null;
        }
    };

    public InfoTab() {}

    public static Fragment newInstance(Person person, ArrayList<String> images) {
        Fragment fragment = new InfoTab();
        Bundle args = new Bundle();
        args.putSerializable("person", person);
        args.putSerializable("images", images);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle args = getArguments();
        if (args != null) {
            mPerson = (Person) args.getSerializable("person");
            mImages = (ArrayList<String>) args.getSerializable("images");
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mPersonDetailsReceiver,
                new IntentFilter("person_details_received"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actor_info, container, false);

        mBiography = view.findViewById(R.id.biography);
        mGender = view.findViewById(R.id.gender);
        mBirthday = view.findViewById(R.id.birthday);
        mDeathday = view.findViewById(R.id.deathday);
        mDeathdayHolder = view.findViewById(R.id.deathday_holder);
        mOfficialSite = view.findViewById(R.id.official_site);
        mPlaceOfBirth = view.findViewById(R.id.place_of_birth);
        mAlsoKnownAs = view.findViewById(R.id.also_known_as);
        mImagesRecyclerView = view.findViewById(R.id.images_recycler_view);
        mKnownForRecyclerView = view.findViewById(R.id.known_for_recycler_view);

        mImagesContainer = view.findViewById(R.id.images_container);
        mKnownForContainer = view.findViewById(R.id.known_for_container);

        HorizontalSpaceItemDecoration dividerItemDecoration = new HorizontalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.list_horizontal_margin));
        mImagesRecyclerView.addItemDecoration(dividerItemDecoration);
        mKnownForRecyclerView.addItemDecoration(dividerItemDecoration);

        if (mPerson.getBiography() != null && !mPerson.getBiography().isEmpty())
            mBiography.setText(mPerson.getBiography());
        else
            mBiography.setText(getString(R.string.no_biography) + " " + mPerson.getName());

        switch (mPerson.getGender()) {
            case 1:
                mGender.setText(R.string.female);
                break;
            case 2:
                mGender.setText(R.string.male);
                break;
            default:
                mGender.setText(R.string.unknown);
                break;
        }

        if (mPerson.getBirthday() != null && !mPerson.getBirthday().isEmpty()) {
            DateTime bDate = DateTimeFormat.forPattern("yyyy-mm-dd").parseDateTime(mPerson.getBirthday());
            mBirthday.setText(DateTimeFormat.longDate().print(bDate.toLocalDate()));
        }
        if (mPerson.getDeathday() != null && !mPerson.getDeathday().isEmpty()) {
            mDeathdayHolder.setVisibility(View.VISIBLE);
            DateTime dDate = DateTimeFormat.forPattern("yyyy-mm-dd").parseDateTime(mPerson.getDeathday());
            mDeathday.setText(DateTimeFormat.longDate().print(dDate.toLocalDate()));
        } else {
            mDeathdayHolder.setVisibility(View.GONE);
        }
        mOfficialSite.setText(mPerson.getHomepage());
        mPlaceOfBirth.setText(mPerson.getPlaceOfBirth());

        if ( mPerson.getAlsoKnownAs() != null) {
            StringBuilder alsoKnownAsStr = new StringBuilder();
            for (int i = 0; i < mPerson.getAlsoKnownAs().length; i++) {
                if (i < mPerson.getAlsoKnownAs().length - 1) {
                    alsoKnownAsStr.append(mPerson.getAlsoKnownAs()[i]).append(", ");
                } else {
                    alsoKnownAsStr.append(mPerson.getAlsoKnownAs()[i]);
                }
            }
            if (alsoKnownAsStr.length() > 0)
                mAlsoKnownAs.setText(alsoKnownAsStr.toString());
        }

        if (mImages != null && !mImages.isEmpty()) {
            mImagesContainer.setVisibility(View.VISIBLE);
            mImagesRecyclerView.setAdapter(new ImagesAdapter(mPerson, mImages));
        } else
            mImagesContainer.setVisibility(View.GONE);

        if (mPerson.getKnownFor() != null && mPerson.getKnownFor().length > 0) {
            mKnownForContainer.setVisibility(View.VISIBLE);
            mKnownForRecyclerView.setAdapter(new KnownForAdapter());
        } else
            mKnownForContainer.setVisibility(View.GONE);

        return view;
    }

    public void onDestroy(){
        super.onDestroy();
        if (mPersonDetailsReceiver != null)
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPersonDetailsReceiver);
    }

    private class KnownForAdapter extends RecyclerView.Adapter<KnownForAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_horizontal_list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (mPerson.getKnownFor()[position] instanceof Movie) {
                Movie movie = (Movie) mPerson.getKnownFor()[position];
                Picasso.with(holder.poster.getContext())
                        .load("http://image.tmdb.org/t/p/w342" + movie.getPosterPath())
                        .placeholder(R.drawable.no_poster)
                        .error(R.drawable.no_poster)
                        .into(holder.poster);
                holder.title.setText(movie.getTitle());
                holder.releaseDate.setText(movie.getReleaseDate().substring(0, 4));
            } else if (mPerson.getKnownFor()[position] instanceof TVShow) {
                TVShow tvShow = (TVShow) mPerson.getKnownFor()[position];
                Picasso.with(holder.poster.getContext())
                        .load("http://image.tmdb.org/t/p/w342" + tvShow.getPosterPath())
                        .placeholder(R.drawable.no_poster)
                        .error(R.drawable.no_poster)
                        .into(holder.poster);
                holder.title.setText(tvShow.getName());
                if (tvShow.getFirstAirDate() != null && tvShow.getFirstAirDate().length() >= 4)
                    holder.releaseDate.setText(tvShow.getFirstAirDate().substring(0, 4));
                else
                    holder.releaseDate.setText(tvShow.getFirstAirDate());
            }
        }

        @Override
        public int getItemCount() {
            return mPerson.getKnownFor().length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private ImageView poster;
            private TextView title;
            private TextView releaseDate;

            ViewHolder(View itemView) {
                super(itemView);

                poster = itemView.findViewById(R.id.poster);
                title = itemView.findViewById(R.id.title);
                releaseDate = itemView.findViewById(R.id.release_date);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mPerson.getKnownFor()[getAdapterPosition()] instanceof Movie) {
                    Movie movie = (Movie) mPerson.getKnownFor()[getAdapterPosition()];
                    Intent intent = new Intent(view.getContext(), MovieActivity.class);
                    intent.putExtra("movie", movie);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        View poster = view.findViewById(R.id.poster);
                        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), poster, "poster_transition");
                        view.getContext().startActivity(intent, activityOptions.toBundle());
                    } else {
                        view.getContext().startActivity(intent);
                    }
                } else if (mPerson.getKnownFor()[getAdapterPosition()] instanceof TVShow) {
                    TVShow tvShow = (TVShow) mPerson.getKnownFor()[getAdapterPosition()];

                    Intent intent = new Intent(view.getContext(), TVShowActivity.class);
                    intent.putExtra("tv_show", tvShow);

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
}
