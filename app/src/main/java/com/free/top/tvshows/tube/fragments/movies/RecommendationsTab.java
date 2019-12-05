package com.free.top.tvshows.tube.fragments.movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.adapters.MoviesAdapter;
import com.free.top.tvshows.tube.api.model.Movie;

import java.util.ArrayList;

public class RecommendationsTab extends Fragment {

    private ArrayList<Movie> mRecommendations;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    public RecommendationsTab() {}

    public static Fragment newInstance(ArrayList<Movie> recommendations) {
        Fragment fragment = new RecommendationsTab();
        Bundle args = new Bundle();
        args.putSerializable("recommendations", recommendations);
        fragment.setArguments(args);
        return fragment;
    }

    private BroadcastReceiver mMovieDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mRecommendations = (ArrayList<Movie>) intent.getSerializableExtra("recommendations");
            mRecyclerView.setAdapter(new MoviesAdapter(mRecommendations));
            mSwipeRefreshLayout.setRefreshing(false);

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMovieDetailsReceiver);
            mMovieDetailsReceiver = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mRecommendations = (ArrayList<Movie>) args.getSerializable("recommendations");
        }
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMovieDetailsReceiver,
                new IntentFilter("movie_details_received"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        if (mRecommendations != null) {
            mRecyclerView.setAdapter(new MoviesAdapter(mRecommendations));
        } else {
            mSwipeRefreshLayout.setEnabled(true);
        }

        return view;
    }

    public void onDestroy(){
        super.onDestroy();
        if (mMovieDetailsReceiver != null)
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMovieDetailsReceiver);
    }
}