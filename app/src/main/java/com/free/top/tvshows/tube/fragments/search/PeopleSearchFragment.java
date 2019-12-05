package com.free.top.tvshows.tube.fragments.search;

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
import android.widget.TextView;

import com.free.top.tvshows.tube.adapters.PeopleAdapter;
import com.free.top.tvshows.tube.api.TMDb;
import com.free.top.tvshows.tube.api.model.Person;
import com.free.top.tvshows.tube.listeners.ListInteractionsListener;

import java.util.ArrayList;
import java.util.List;

public class PeopleSearchFragment extends Fragment implements TMDb.Listener<Person>, ListInteractionsListener {

    private String mQuery;

    private List<Person> mDataSet = new ArrayList<>();

    private int page = -1;
    private int totalPages = -1;
    private boolean updating = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.Adapter mAdapter;

    private View mErrorContainer;
    private View mRetryButton;
    private TextView mErrorDescription;

    public PeopleSearchFragment() {}

    public static Fragment newInstance(String query) {
        Fragment fragment = new PeopleSearchFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle args = getArguments();
        if (args != null) {
            mQuery = args.getString("query");
            getData(1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        mErrorContainer = view.findViewById(R.id.error_container);
        mErrorDescription = view.findViewById(R.id.error_description);
        mRetryButton = view.findViewById(R.id.retry_button);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
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
        mAdapter = new PeopleAdapter(mDataSet, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        if (!updating && mDataSet.isEmpty() && page == -1) {
            mErrorDescription.setText(R.string.error_description);
            mRetryButton.setVisibility(View.VISIBLE);
            mErrorContainer.setVisibility(View.VISIBLE);
        } else if (!updating && mDataSet.isEmpty()) {
            mErrorDescription.setText(R.string.nothing_found);
            mRetryButton.setVisibility(View.GONE);
            mErrorContainer.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResponse(List<Person> data, int page, int totalPages) {
        this.page = page;
        this.totalPages = totalPages;
        updating = false;
        mDataSet.addAll(data);
        if (getView() != null) {
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(updating);

            if (mDataSet.isEmpty()) {
                mErrorDescription.setText(R.string.nothing_found);
                mRetryButton.setVisibility(View.GONE);
                mErrorContainer.setVisibility(View.VISIBLE);
            }
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

        TMDb.Search.searchPeople(mQuery, page, this);
    }
}
