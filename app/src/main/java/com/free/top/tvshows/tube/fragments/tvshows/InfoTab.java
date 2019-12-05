package com.free.top.tvshows.tube.fragments.tvshows;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.adapters.TVShowsAdapter;
import com.free.top.tvshows.tube.adapters.VideosAdapter;
import com.free.top.tvshows.tube.api.OMDb;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.free.top.tvshows.tube.api.model.Video;
import com.free.top.tvshows.tube.utils.HorizontalSpaceItemDecoration;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.util.ArrayList;
import java.util.Locale;

public class InfoTab extends Fragment {

    private TVShow mTVShow;
    private ArrayList<Video> mVideos;
    private ArrayList<TVShow> mSimilar;
    private ArrayList<TVShow> mRecommendations;
    private double imdbRating = -1;
    private int metascore = -1;

    private TextView mTMDbRating;
    private TextView mIMDbRating;
    private TextView mMetacriticRating;
    private TextView mOverview;
    private TextView mStatus;
    private TextView mNetworks;
    private TextView mOriginalLanguage;
    private TextView mRuntime;
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

    private BroadcastReceiver mTVShowDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TVShow tvShow = (TVShow) intent.getSerializableExtra("tv_show");
            if (mTVShow.getId() == tvShow.getId()) {
                mTVShow = tvShow;
                mVideos = (ArrayList<Video>) intent.getSerializableExtra("videos");
                mSimilar = (ArrayList<TVShow>) intent.getSerializableExtra("similar");
                mRecommendations = (ArrayList<TVShow>) intent.getSerializableExtra("recommendations");

                if (mTVShow.getVoteAverage() > 0)
                    mTMDbRating.setText(String.format("%.1f", mTVShow.getVoteAverage()));

                mOverview.setText(mTVShow.getOverview());

                mStatus.setText(mTVShow.getStatus());
                if (mTVShow.getNetworks() != null) {
                    StringBuilder networksStr = new StringBuilder();
                    for (int i = 0; i < mTVShow.getNetworks().length; i++) {
                        if (i != mTVShow.getNetworks().length - 1) {
                            networksStr.append(mTVShow.getNetworks()[i].getName()).append(", ");
                        } else {
                            networksStr.append(mTVShow.getNetworks()[i].getName());
                        }
                    }
                    mNetworks.setText(networksStr.toString());
                }
                if (mTVShow.getOriginalLanguage() != null) {
                    Locale locale = new Locale(mTVShow.getOriginalLanguage());
                    mOriginalLanguage.setText(locale.getDisplayLanguage());
                }
                if (mTVShow.getEpisodeRunTime() != null) {
                    StringBuilder runtimeStr = new StringBuilder();
                    Locale defaultLocale = Locale.getDefault();
                    for (int i = 0; i < mTVShow.getEpisodeRunTime().length; i++) {
                        Period period = new Period(mTVShow.getEpisodeRunTime()[i] * 60 * 1000L);
                        if (i != mTVShow.getEpisodeRunTime().length - 1) {
                            runtimeStr.append(PeriodFormat.wordBased(defaultLocale).print(period)).append(", ");
                        } else {
                            runtimeStr.append(PeriodFormat.wordBased(defaultLocale).print(period));
                        }
                    }
                    mRuntime.setText(runtimeStr.toString());
                }

                if (mVideos != null && !mVideos.isEmpty()) {
                    mVideosContainer.setVisibility(View.VISIBLE);
                    mVideosRecyclerView.setAdapter(new VideosAdapter(mVideos));
                } else
                    mVideosContainer.setVisibility(View.GONE);

                if (mSimilar != null && !mSimilar.isEmpty()) {
                    mSimilarContainer.setVisibility(View.VISIBLE);
                    mSimilarRecyclerView.setAdapter(new TVShowsAdapter(mSimilar, true));
                } else
                    mSimilarContainer.setVisibility(View.GONE);

                if (mRecommendations != null && !mRecommendations.isEmpty()) {
                    mRecommendationsContainer.setVisibility(View.VISIBLE);
                    mRecommendationsRecyclerView.setAdapter(new TVShowsAdapter(mRecommendations, true));
                } else
                    mRecommendationsContainer.setVisibility(View.GONE);

                if (mTVShow.getImdbId() != null)
                    OMDb.getRating(mTVShow.getImdbId(), ratingListener);

                LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTVShowDetailsReceiver);
                mTVShowDetailsReceiver = null;
            }

        }
    };

    public InfoTab() {}

    public static Fragment newInstance(TVShow tvShow, ArrayList<Video> videos, ArrayList<TVShow> similar, ArrayList<TVShow> recommendations) {
        Fragment fragment = new InfoTab();
        Bundle args = new Bundle();
        args.putSerializable("tv_show", tvShow);
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
            mTVShow = (TVShow) args.getSerializable("tv_show");
            mVideos = (ArrayList<Video>) args.getSerializable("videos");
            mSimilar = (ArrayList<TVShow>) args.getSerializable("similar");
            mRecommendations = (ArrayList<TVShow>) args.getSerializable("recommendations");
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTVShowDetailsReceiver,
                new IntentFilter("tv_show_details_received"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_show_info, container, false);

        mTMDbRating = view.findViewById(R.id.tmdb_rating);
        if (mTVShow.getVoteAverage() > 0)
            mTMDbRating.setText(String.format("%.1f", mTVShow.getVoteAverage()));

        mIMDbRating = view.findViewById(R.id.imdb_rating);
        mMetacriticRating = view.findViewById(R.id.metacritic_rating);

        if (imdbRating >= 0)
            mIMDbRating.setText(String.format("%.1f", imdbRating));

        if (metascore >=0)
            mMetacriticRating.setText(String.valueOf(metascore));

        mOverview = view.findViewById(R.id.overview);
        mOverview.setText(mTVShow.getOverview());

        TextView originalTitle = view.findViewById(R.id.original_title);
        originalTitle.setText(mTVShow.getOriginalName());

        mVideosContainer = view.findViewById(R.id.videos_container);
        mSimilarContainer = view.findViewById(R.id.similar_container);
        mRecommendationsContainer = view.findViewById(R.id.recommendations_container);

        mStatus = view.findViewById(R.id.status);
        mStatus.setText(mTVShow.getStatus());

        mNetworks = view.findViewById(R.id.network);
        if (mTVShow.getNetworks() != null) {
            StringBuilder networksStr = new StringBuilder();
            for (int i = 0; i < mTVShow.getNetworks().length; i++) {
                if (i != mTVShow.getNetworks().length - 1) {
                    networksStr.append(mTVShow.getNetworks()[i].getName()).append(", ");
                } else {
                    networksStr.append(mTVShow.getNetworks()[i].getName());
                }
            }
            mNetworks.setText(networksStr.toString());
        }

        mOriginalLanguage = view.findViewById(R.id.original_language);
        if (mTVShow.getOriginalLanguage() != null) {
            Locale locale = new Locale(mTVShow.getOriginalLanguage());
            mOriginalLanguage.setText(locale.getDisplayLanguage());
        }

        mRuntime = view.findViewById(R.id.runtime);
        if (mTVShow.getEpisodeRunTime() != null) {
            StringBuilder runtimeStr = new StringBuilder();
            Locale defaultLocale = Locale.getDefault();
            for (int i = 0; i < mTVShow.getEpisodeRunTime().length; i++) {
                Period period = new Period(mTVShow.getEpisodeRunTime()[i] * 60 * 1000L);
                if (i != mTVShow.getEpisodeRunTime().length - 1) {
                    runtimeStr.append(PeriodFormat.wordBased(defaultLocale).print(period)).append(", ");
                } else {
                    runtimeStr.append(PeriodFormat.wordBased(defaultLocale).print(period));
                }
            }
            mRuntime.setText(runtimeStr.toString());
        }

        mVideosRecyclerView = view.findViewById(R.id.videos_recycler_view);
        HorizontalSpaceItemDecoration dividerItemDecoration = new HorizontalSpaceItemDecoration(dpToPx(16));
        mVideosRecyclerView.addItemDecoration(dividerItemDecoration);
        if (mVideos != null && !mVideos.isEmpty()) {
            mVideosContainer.setVisibility(View.VISIBLE);
            mVideosRecyclerView.setAdapter(new VideosAdapter(mVideos));
        } else
            mVideosContainer.setVisibility(View.GONE);

        mSimilarRecyclerView = view.findViewById(R.id.similar_recycler_view);
        mSimilarRecyclerView.addItemDecoration(dividerItemDecoration);
        if (mSimilar != null && !mSimilar.isEmpty()) {
            mSimilarContainer.setVisibility(View.VISIBLE);
            mSimilarRecyclerView.setAdapter(new TVShowsAdapter(mSimilar, true));
        } else
            mSimilarContainer.setVisibility(View.GONE);

        mRecommendationsRecyclerView = view.findViewById(R.id.recommendations_recycler_view);
        mRecommendationsRecyclerView.addItemDecoration(dividerItemDecoration);
        if (mRecommendations != null && !mRecommendations.isEmpty()) {
            mRecommendationsContainer.setVisibility(View.VISIBLE);
            mRecommendationsRecyclerView.setAdapter(new TVShowsAdapter(mRecommendations, true));
        } else
            mRecommendationsContainer.setVisibility(View.GONE);

        return view;
    }

    public void onDestroy(){
        super.onDestroy();
        if (mTVShowDetailsReceiver != null)
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTVShowDetailsReceiver);
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
