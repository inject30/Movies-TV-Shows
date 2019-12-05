package com.free.top.tvshows.tube.fragments.tvshows;

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
import com.free.top.tvshows.tube.adapters.TVSeasonsAdapter;
import com.free.top.tvshows.tube.api.model.TVShow;

public class SeasonsTab extends Fragment {

    private TVShow mTVShow;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private BroadcastReceiver mTVShowDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTVShow = (TVShow) intent.getSerializableExtra("tv_show");
            mRecyclerView.setAdapter(new TVSeasonsAdapter(mTVShow));
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);

            if (mTVShow.getSeasons() != null) {
                mRecyclerView.setAdapter(new TVSeasonsAdapter(mTVShow));
            } else {
                mSwipeRefreshLayout.setEnabled(true);
            }

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTVShowDetailsReceiver);
            mTVShowDetailsReceiver = null;
        }
    };

    public SeasonsTab() {}

    public static Fragment newInstance(TVShow tvShow) {
        Fragment fragment = new SeasonsTab();
        Bundle args = new Bundle();
        args.putSerializable("tv_show", tvShow);
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
        }
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTVShowDetailsReceiver,
                new IntentFilter("tv_show_details_received"));
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

        if (mTVShow.getSeasons() != null) {
            mRecyclerView.setAdapter(new TVSeasonsAdapter(mTVShow));
        } else {
            mSwipeRefreshLayout.setEnabled(true);
        }

        return view;
    }

    public void onDestroy(){
        super.onDestroy();
        if (mTVShowDetailsReceiver != null)
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTVShowDetailsReceiver);
    }
}
