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
import com.free.top.tvshows.tube.adapters.MovieCreditsAdapter;
import com.free.top.tvshows.tube.api.model.CastCredit;
import com.free.top.tvshows.tube.api.model.CrewCredit;

import java.util.ArrayList;

public class MovieCreditsTab extends Fragment {

    private ArrayList<CastCredit> mMovieCast;
    private ArrayList<CrewCredit> mMovieCrew;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private BroadcastReceiver mPersonDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mMovieCast = (ArrayList<CastCredit>) intent.getSerializableExtra("movie_cast");
            mMovieCrew = (ArrayList<CrewCredit>) intent.getSerializableExtra("movie_crew");
            mSwipeRefreshLayout.setEnabled(false);
            mRecyclerView.setAdapter(new MovieCreditsAdapter(mMovieCast, mMovieCrew));

            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mPersonDetailsReceiver);
            mPersonDetailsReceiver = null;
        }
    };

    public MovieCreditsTab() {}

    public static Fragment newInstance(ArrayList<CastCredit> movieCast, ArrayList<CrewCredit> movieCrew) {
        Fragment fragment = new MovieCreditsTab();
        Bundle args = new Bundle();
        args.putSerializable("movie_cast", movieCast);
        args.putSerializable("movie_crew", movieCrew);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle args = getArguments();
        if (args != null) {
            mMovieCast = (ArrayList<CastCredit>) args.getSerializable("movie_cast");
            mMovieCrew = (ArrayList<CrewCredit>) args.getSerializable("movie_crew");
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
        if (mMovieCast != null && mMovieCrew != null) {
            mRecyclerView.setAdapter(new MovieCreditsAdapter(mMovieCast, mMovieCrew));
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
