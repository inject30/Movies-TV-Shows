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
import com.free.top.tvshows.tube.adapters.CastsAdapter;
import com.free.top.tvshows.tube.api.model.Cast;

import java.util.ArrayList;


public class CastTab extends Fragment {

    private ArrayList<Cast> mCasts;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private BroadcastReceiver mTVShowDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCasts = (ArrayList<Cast>) intent.getSerializableExtra("casts");
            mRecyclerView.setAdapter(new CastsAdapter(mCasts));
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTVShowDetailsReceiver);
            mTVShowDetailsReceiver = null;
        }
    };

    public CastTab() {}

    public static Fragment newInstance(ArrayList<Cast> casts) {
        Fragment fragment = new CastTab();
        Bundle args = new Bundle();
        args.putSerializable("casts", casts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle args = getArguments();
        if (args != null) {
            mCasts = (ArrayList<Cast>) args.getSerializable("casts");
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
        if (mCasts != null) {
            mRecyclerView.setAdapter(new CastsAdapter(mCasts));
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
