package com.free.top.tvshows.tube.fragments.movies;

import android.os.Bundle;

import com.free.top.tvshows.tube.R;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.free.top.tvshows.tube.adapters.MoviesAdapter;
import com.free.top.tvshows.tube.api.TMDb;
import com.free.top.tvshows.tube.api.model.Movie;
import com.free.top.tvshows.tube.listeners.ListInteractionsListener;

import java.util.ArrayList;
import java.util.List;

public class MoviesTab extends Fragment implements TMDb.Listener<Movie>, ListInteractionsListener {

    public enum Type {
        NOW_PLAYING,
        POPULAR,
        TOP_RATED,
        UPCOMING
    };

    private static final String TYPE_KEY = "type";

    private Type type;
    private int page = -1;
    private int totalPages = -1;
    private boolean updating = false;
    private List<Movie> mDataSet = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.Adapter mAdapter;

    private View mErrorContainer;

    public MoviesTab() {}

    public static Fragment newInstance(Type type) {
        Fragment fragment = new MoviesTab();
        Bundle args = new Bundle();
        args.putSerializable(TYPE_KEY, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle args = getArguments();
        if (args != null) {
            type = (Type) args.getSerializable(TYPE_KEY);
            getData(1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        mErrorContainer = view.findViewById(R.id.error_container);
        View retryButton = view.findViewById(R.id.retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mErrorContainer.setVisibility(View.GONE);
                getData(1);
            }
        });

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(updating);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new MoviesAdapter(mDataSet, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        if (!updating && mDataSet.isEmpty())
            mErrorContainer.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onResponse(List<Movie> data, int page, int totalPages) {
        this.page = page;
        this.totalPages = totalPages;
        updating = false;
        mDataSet.addAll(data);
        if (getView() != null) {
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(updating);
        }
    }

    @Override
    public void onError() {
        updating = false;
        if (getView() != null) {
            mSwipeRefreshLayout.setRefreshing(updating);

            if (mDataSet.isEmpty()) {
                mErrorContainer.setVisibility(View.VISIBLE);
            } else {
                Snackbar.make(getView(), R.string.error_description, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onListEndReached() {
        if (!updating && page < totalPages)
            getData(page + 1);
    }

    private void getData(int page) {
        updating = true;
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(updating);
        switch (type) {
            case NOW_PLAYING:
                TMDb.Movies.getNowPlaying(page, this);
                break;
            case POPULAR:
                TMDb.Movies.getPopular(page, this);
                break;
            case TOP_RATED:
                TMDb.Movies.getTopRated(page, this);
                break;
            case UPCOMING:
                TMDb.Movies.getUpcoming(page, this);
                break;
        }

    }
}