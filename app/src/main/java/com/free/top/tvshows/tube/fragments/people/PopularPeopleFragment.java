package com.free.top.tvshows.tube.fragments.people;

import android.os.Bundle;

import com.free.top.tvshows.tube.R;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.free.top.tvshows.tube.adapters.PeopleAdapter;
import com.free.top.tvshows.tube.api.TMDb;
import com.free.top.tvshows.tube.api.model.Person;
import com.free.top.tvshows.tube.listeners.ListInteractionsListener;

import java.util.ArrayList;
import java.util.List;

public class PopularPeopleFragment extends Fragment implements TMDb.Listener<Person>, ListInteractionsListener {

    private int page = -1;
    private int totalPages = -1;
    private boolean updating = false;
    private List<Person> mDataSet = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    public PopularPeopleFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        getData(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.popular_people);

        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        TabLayout tabLayout = getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.GONE);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(updating);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new PeopleAdapter(mDataSet, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    @Override
    public void onResponse(List<Person> data, int page, int totalPages) {
        this.page = page;
        this.totalPages = totalPages;
        updating = false;
        mDataSet.addAll(data);
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(updating);
    }

    @Override
    public void onError() {
        updating = false;
        mSwipeRefreshLayout.setRefreshing(updating);
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

        TMDb.People.getPopular(page, this);
    }


}
