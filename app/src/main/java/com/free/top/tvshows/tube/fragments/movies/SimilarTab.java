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

public class SimilarTab extends Fragment {

    private ArrayList<Movie> mSimilarMovies;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private BroadcastReceiver mMovieDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSimilarMovies = (ArrayList<Movie>) intent.getSerializableExtra("similar");
            mRecyclerView.setAdapter(new MoviesAdapter(mSimilarMovies));
            mSwipeRefreshLayout.setRefreshing(false);

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMovieDetailsReceiver);
            mMovieDetailsReceiver = null;
        }
    };

    public SimilarTab() {}

    public static Fragment newInstance(ArrayList<Movie> similar) {
        Fragment fragment = new SimilarTab();
        Bundle args = new Bundle();
        args.putSerializable("similar", similar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mSimilarMovies = (ArrayList<Movie>) args.getSerializable("similar");
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
        if (mSimilarMovies != null) {
            mRecyclerView.setAdapter(new MoviesAdapter(mSimilarMovies));
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