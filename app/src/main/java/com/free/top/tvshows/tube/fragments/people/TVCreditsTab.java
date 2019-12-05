package com.free.top.tvshows.tube.fragments.people;

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
import com.free.top.tvshows.tube.adapters.TVCreditsAdapter;
import com.free.top.tvshows.tube.api.model.TVCastCredit;
import com.free.top.tvshows.tube.api.model.TVCrewCredit;

import java.util.ArrayList;

public class TVCreditsTab extends Fragment {

    private ArrayList<TVCastCredit> mTVCast;
    private ArrayList<TVCrewCredit> mTVCrew;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private BroadcastReceiver mPersonDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mTVCast = (ArrayList<TVCastCredit>) intent.getSerializableExtra("tv_cast");
            mTVCrew = (ArrayList<TVCrewCredit>) intent.getSerializableExtra("tv_crew");
            mSwipeRefreshLayout.setEnabled(false);

            mRecyclerView.setAdapter(new TVCreditsAdapter(mTVCast, mTVCrew));

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPersonDetailsReceiver);
            mPersonDetailsReceiver = null;
        }
    };


    public  TVCreditsTab() {}

    public static Fragment newInstance(ArrayList<TVCastCredit> tvCast, ArrayList<TVCrewCredit> tvCrew) {
        Fragment fragment = new TVCreditsTab();
        Bundle args = new Bundle();
        args.putSerializable("tv_cast", tvCast);
        args.putSerializable("tv_crew", tvCrew);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle args = getArguments();
        if (args != null) {
            mTVCast = (ArrayList<TVCastCredit>) args.getSerializable("tv_cast");
            mTVCrew = (ArrayList<TVCrewCredit>) args.getSerializable("tv_crew");
        }

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mPersonDetailsReceiver,
                new IntentFilter("person_details_received"));
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
        if (mTVCast != null && mTVCrew != null) {
            mRecyclerView.setAdapter(new TVCreditsAdapter(mTVCast, mTVCrew));
        } else {
            mSwipeRefreshLayout.setEnabled(true);
        }

        return view;
    }

    public void onDestroy(){
        super.onDestroy();
        if (mPersonDetailsReceiver != null)
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPersonDetailsReceiver);
    }
}
