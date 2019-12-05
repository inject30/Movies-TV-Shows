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
import com.free.top.tvshows.tube.adapters.ReviewsAdapter;
import com.free.top.tvshows.tube.api.model.Review;

import java.util.ArrayList;

public class ReviewsTab extends Fragment {

    private ArrayList<Review> mReviews;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private BroadcastReceiver mMovieDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mReviews = (ArrayList<Review>) intent.getSerializableExtra("reviews");
            mRecyclerView.setAdapter(new ReviewsAdapter(mReviews));
            mSwipeRefreshLayout.setRefreshing(false);

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMovieDetailsReceiver);
            mMovieDetailsReceiver = null;
        }
    };

    public ReviewsTab() {}

    public static Fragment newInstance(ArrayList<Review> reviews) {
        Fragment fragment = new ReviewsTab();
        Bundle args = new Bundle();
        args.putSerializable("reviews", reviews);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mReviews = (ArrayList<Review>) args.getSerializable("reviews");
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
        if (mReviews != null) {
            mRecyclerView.setAdapter(new ReviewsAdapter(mReviews));
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