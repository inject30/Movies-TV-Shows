package com.free.top.tvshows.tube.fragments.watchlist;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.adapters.TVShowsAdapter;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.free.top.tvshows.tube.database.TVShowDAO;

import java.util.ArrayList;
import java.util.List;

public class TVShowsWatchlistTab extends Fragment {

    private List<TVShow> mDataSet = new ArrayList<>();

    public TVShowsWatchlistTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        TVShowDAO tvShowDAO = new TVShowDAO(getContext());

        mDataSet = tvShowDAO.getTVShows();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.Adapter adapter = new TVShowsAdapter(mDataSet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        if (mDataSet.isEmpty()) {
            View errorContainer = view.findViewById(R.id.error_container);
            TextView errorDescription = view.findViewById(R.id.error_description);
            View retryButton = view.findViewById(R.id.retry_button);

            errorDescription.setText(R.string.tv_shows_watchlist_empty);
            retryButton.setVisibility(View.GONE);
            errorContainer.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
