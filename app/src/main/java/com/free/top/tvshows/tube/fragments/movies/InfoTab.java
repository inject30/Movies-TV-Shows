package com.free.top.tvshows.tube.fragments.movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.adapters.MoviesAdapter;
import com.free.top.tvshows.tube.adapters.VideosAdapter;
import com.free.top.tvshows.tube.api.OMDb;
import com.free.top.tvshows.tube.api.model.Movie;
import com.free.top.tvshows.tube.api.model.Video;
import com.free.top.tvshows.tube.utils.HorizontalSpaceItemDecoration;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class InfoTab extends Fragment {

    private boolean dataReceived = false;

    private Movie mMovie;
    private ArrayList<Video> mVideos;
    private ArrayList<Movie> mSimilarMovies;
    private ArrayList<Movie> mRecommendations;
    private double imdbRating = -1;
    private int metascore = -1;

    private TextView mTMDbRating;
    private TextView mIMDbRating;
    private TextView mMetacriticRating;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTagline;
    private TextView mOverview;
    private TextView mStatus;
    private TextView mOriginalLanguage;
    private TextView mRuntime;
    private TextView mBudget;
    private TextView mRevenue;
    private RecyclerView mVideosRecyclerView;
    private RecyclerView mSimilarRecyclerView;
    private RecyclerView mRecommendationsRecyclerView;

    private View mVideosContainer;
    private View mSimilarContainer;
    private View mRecommendationsContainer;

    private OMDb.RatingListener ratingListener = new OMDb.RatingListener() {
        @Override
        public void onSuccess(double imdbRating, int metascore) {
            InfoTab.this.imdbRating = imdbRating;
            InfoTab.this.metascore = metascore;

            if (mIMDbRating != null && imdbRating > 0)
                mIMDbRating.setText(String.format("%.1f", imdbRating));

            if (mMetacriticRating != null && metascore > 0)
                mMetacriticRating.setText(String.valueOf(metascore));
        }

        @Override
        public void onError() {

        }
    };

    private BroadcastReceiver mMovieDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dataReceived = true;
            mMovie = (Movie) intent.getSerializableExtra("movie");
            mVideos = (ArrayList<Video>) intent.getSerializableExtra("videos");
            mSimilarMovies = (ArrayList<Movie>) intent.getSerializableExtra("similar");
            mRecommendations = (ArrayList<Movie>) intent.getSerializableExtra("recommendations");
            OMDb.getRating(mMovie.getImdbId(), ratingListener);

            if (mMovie.getVoteAverage() > 0)
                mTMDbRating.setText(String.format("%.1f", mMovie.getVoteAverage()));

            if (mMovie.getTagline() != null && !mMovie.getTagline().isEmpty()) {
                mTagline.setVisibility(View.VISIBLE);
                mTagline.setText(mMovie.getTagline());
            } else
                mTagline.setVisibility(View.GONE);

            if (mMovie.getOverview() != null && !mMovie.getOverview().isEmpty()) {
                mOverview.setVisibility(View.VISIBLE);
                mOverview.setText(mMovie.getOverview());
            } else
                mOverview.setVisibility(View.GONE);

            mStatus.setText(mMovie.getStatus());

            if (mMovie.getOriginalLanguage() != null) {
                Locale locale = new Locale(mMovie.getOriginalLanguage());
                mOriginalLanguage.setText(locale.getDisplayLanguage());
            }

            if (mMovie.getRuntime() > 0) {
                Period period = new Period(mMovie.getRuntime() * 60 * 1000L);
                mRuntime.setText(PeriodFormat.wordBased(Locale.getDefault()).print(period));
            }

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            numberFormat.setCurrency(Currency.getInstance("USD"));
            if (mMovie.getBudget() > 0)
                mBudget.setText(numberFormat.format(mMovie.getBudget()));
            if (mMovie.getRevenue() > 0)
                mRevenue.setText(numberFormat.format(mMovie.getRevenue()));

            if (mVideos != null && !mVideos.isEmpty()) {
                mVideosContainer.setVisibility(View.VISIBLE);
                mVideosRecyclerView.setAdapter(new VideosAdapter(mVideos));
            } else
                mVideosContainer.setVisibility(View.GONE);

            if (mSimilarMovies != null && !mSimilarMovies.isEmpty()) {
                mSimilarContainer.setVisibility(View.VISIBLE);
                mSimilarRecyclerView.setAdapter(new MoviesAdapter(mSimilarMovies, MoviesAdapter.HORIZONTAL));
            } else
                mSimilarContainer.setVisibility(View.GONE);

            if (mRecommendations != null && !mRecommendations.isEmpty()) {
                mRecommendationsContainer.setVisibility(View.VISIBLE);
                mRecommendationsRecyclerView.setAdapter(new MoviesAdapter(mRecommendations, MoviesAdapter.HORIZONTAL));
            } else
                mRecommendationsContainer.setVisibility(View.GONE);

            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMovieDetailsReceiver);
            mMovieDetailsReceiver = null;
        }
    };

    public static Fragment newInstance(Movie movie, ArrayList<Video> videos, ArrayList<Movie> similar, ArrayList<Movie> recommendations) {
        Fragment fragment = new InfoTab();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);
        args.putSerializable("videos", videos);
        args.putSerializable("similar", similar);
        args.putSerializable("recommendations", recommendations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle args = getArguments();
        if (args != null) {
            mMovie = (Movie) args.getSerializable("movie");
            mVideos = (ArrayList<Video>) args.getSerializable("videos");
            mSimilarMovies = (ArrayList<Movie>) args.getSerializable("similar");
            mRecommendations = (ArrayList<Movie>) args.getSerializable("recommendations");

            if (mMovie.getImdbId() != null)
                OMDb.getRating(mMovie.getImdbId(), ratingListener);
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMovieDetailsReceiver,
                new IntentFilter("movie_details_received"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_info, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);

        mTMDbRating = view.findViewById(R.id.tmdb_rating);
        if (mMovie.getVoteAverage() > 0)
            mTMDbRating.setText(String.format("%.1f", mMovie.getVoteAverage()));

        mIMDbRating = view.findViewById(R.id.imdb_rating);
        mMetacriticRating = view.findViewById(R.id.metacritic_rating);

        if (imdbRating >= 0)
            mIMDbRating.setText(String.format("%.1f", imdbRating));

        if (metascore >=0)
            mMetacriticRating.setText(String.valueOf(metascore));

        mTagline = view.findViewById(R.id.tagline);
        if (mMovie.getTagline() != null && !mMovie.getTagline().isEmpty()) {
            mTagline.setVisibility(View.VISIBLE);
            mTagline.setText(mMovie.getTagline());
        } else
            mTagline.setVisibility(View.GONE);

        mOverview = view.findViewById(R.id.overview);
        if (mMovie.getOverview() != null && !mMovie.getOverview().isEmpty())
            mOverview.setText(mMovie.getOverview());
        else
            mOverview.setVisibility(View.GONE);

        TextView originalTitle = view.findViewById(R.id.original_title);
        originalTitle.setText(mMovie.getOriginalTitle());

        mStatus = view.findViewById(R.id.status);
        mStatus.setText(mMovie.getStatus());

        mOriginalLanguage = view.findViewById(R.id.original_language);
        if (mMovie.getOriginalLanguage() != null) {
            Locale locale = new Locale(mMovie.getOriginalLanguage());
            mOriginalLanguage.setText(locale.getDisplayLanguage());
        }

        mRuntime = view.findViewById(R.id.runtime);
        mBudget = view.findViewById(R.id.budget);
        mRevenue = view.findViewById(R.id.revenue);

        if (mMovie.getRuntime() > 0) {
            Period period = new Period(mMovie.getRuntime() * 60 * 1000L);
            mRuntime.setText(PeriodFormat.wordBased(Locale.getDefault()).print(period));
        }

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setCurrency(Currency.getInstance("USD"));
        if (mMovie.getBudget() > 0)
            mBudget.setText(numberFormat.format(mMovie.getBudget()));
        if (mMovie.getRevenue() > 0)
            mRevenue.setText(numberFormat.format(mMovie.getRevenue()));

        mVideosContainer = view.findViewById(R.id.videos_container);
        mVideosRecyclerView = view.findViewById(R.id.videos_recycler_view);
        HorizontalSpaceItemDecoration dividerItemDecoration = new HorizontalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.list_horizontal_margin));
        mVideosRecyclerView.addItemDecoration(dividerItemDecoration);

        if (mVideos != null && !mVideos.isEmpty()) {
            mVideosContainer.setVisibility(View.VISIBLE);
            mVideosRecyclerView.setAdapter(new VideosAdapter(mVideos));
        } else
            mVideosContainer.setVisibility(View.GONE);

        mSimilarContainer = view.findViewById(R.id.similar_container);
        mSimilarRecyclerView =  view.findViewById(R.id.similar_recycler_view);
        mSimilarRecyclerView.addItemDecoration(dividerItemDecoration);

        if (mSimilarMovies != null && !mSimilarMovies.isEmpty()) {
            mSimilarContainer.setVisibility(View.VISIBLE);
            mSimilarRecyclerView.setAdapter(new MoviesAdapter(mSimilarMovies, MoviesAdapter.HORIZONTAL));
        } else
            mSimilarContainer.setVisibility(View.GONE);

        mRecommendationsContainer = view.findViewById(R.id.recommendations_container);
        mRecommendationsRecyclerView = view.findViewById(R.id.recommendations_recycler_view);
        mRecommendationsRecyclerView.addItemDecoration(dividerItemDecoration);

        if (mRecommendations != null && !mRecommendations.isEmpty()) {
            mRecommendationsContainer.setVisibility(View.VISIBLE);
            mRecommendationsRecyclerView.setAdapter(new MoviesAdapter(mRecommendations, MoviesAdapter.HORIZONTAL));
        } else
            mRecommendationsContainer.setVisibility(View.GONE);

        if (!dataReceived) {
            mSwipeRefreshLayout.setRefreshing(true);
        }


        return view;
    }

    public void onDestroy(){
        super.onDestroy();
        if (mMovieDetailsReceiver != null)
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMovieDetailsReceiver);
    }
}
