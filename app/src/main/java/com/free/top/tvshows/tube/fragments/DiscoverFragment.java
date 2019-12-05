package com.free.top.tvshows.tube.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.free.top.tvshows.tube.Application;
import com.free.top.tvshows.tube.R;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.free.top.tvshows.tube.adapters.MoviesAdapter;
import com.free.top.tvshows.tube.adapters.TVShowsAdapter;
import com.free.top.tvshows.tube.api.TMDb;
import com.free.top.tvshows.tube.api.model.Movie;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.free.top.tvshows.tube.listeners.ListInteractionsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DiscoverFragment extends Fragment implements ListInteractionsListener {

    private int sort = 0;
    private int mediaType = 0;
    private int yearFrom = Calendar.getInstance().get(Calendar.YEAR);
    private int yearTo = yearFrom;
    private int genre = 0;

    private List<Movie> mMoviesDataSet = new ArrayList<>();
    private List<TVShow> mTVShowsDataSet = new ArrayList<>();

    private int page = -1;
    private int totalPages = -1;
    private boolean updating = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private TMDb.Listener<Movie> mMoviesListener = new TMDb.Listener<Movie>() {
        @Override
        public void onResponse(List<Movie> data, int page, int totalPages) {
            DiscoverFragment.this.page = page;
            DiscoverFragment.this.totalPages = totalPages;
            DiscoverFragment.this.updating = false;
            mMoviesDataSet.addAll(data);
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(updating);
        }

        @Override
        public void onError() {

        }
    };

    private TMDb.Listener<TVShow> mTVShowsListener = new TMDb.Listener<TVShow>() {
        @Override
        public void onResponse(List<TVShow> data, int page, int totalPages) {
            DiscoverFragment.this.page = page;
            DiscoverFragment.this.totalPages = totalPages;
            DiscoverFragment.this.updating = false;
            mTVShowsDataSet.addAll(data);
            mAdapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(updating);
        }

        @Override
        public void onError() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        getData(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.discover);

        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        TabLayout tabLayout = getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.GONE);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(updating);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        if (mediaType == 0) {
            mAdapter = new MoviesAdapter(mMoviesDataSet, this);
        } else {
            mAdapter = new TVShowsAdapter(mTVShowsDataSet, this);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_discover, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                int itemsArrayId;
                if (mediaType == 0)
                    itemsArrayId = R.array.movies_sort_options;
                else
                    itemsArrayId = R.array.tv_shows_sort_options;
                new MaterialDialog.Builder(this.getContext())
                        .title(R.string.sort)
                        .iconRes(R.drawable.ic_sort)
                        .items(itemsArrayId)
                        .itemsCallbackSingleChoice(sort, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                sort = which;

                                page = -1;
                                totalPages = -1;
                                mMoviesDataSet.clear();
                                mTVShowsDataSet.clear();
                                mAdapter.notifyDataSetChanged();
                                getData(1);

                                return true;
                            }
                        })
                        .positiveText(R.string.sort)
                        .show();
                return true;
            case R.id.action_filter:
                MaterialDialog dialog = new MaterialDialog.Builder(this.getContext())
                        .title(R.string.filter)
                        .iconRes(R.drawable.ic_filter_list)
                        .customView(R.layout.dialog_filter, true)
                        .positiveText(R.string.filter)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View view = dialog.getView();
                                AppCompatSpinner mediaTypeSpinner = view.findViewById(R.id.media_type_spinner);
                                AppCompatSpinner genreSpinner = view.findViewById(R.id.genre_spinner);
                                NumberPicker yearFromNumberPicker = view.findViewById(R.id.year_from_number_picker);
                                NumberPicker yearToNumberPicker = view.findViewById(R.id.year_to_number_picker);

                                mediaType = mediaTypeSpinner.getSelectedItemPosition();
                                genre = genreSpinner.getSelectedItemPosition();
                                yearFrom = yearFromNumberPicker.getValue();
                                yearTo = yearToNumberPicker.getValue();

                                page = -1;
                                totalPages = -1;
                                mMoviesDataSet.clear();
                                mTVShowsDataSet.clear();

                                if (mediaType == 0) {
                                    mAdapter = new MoviesAdapter(mMoviesDataSet, DiscoverFragment.this);
                                } else {
                                    mAdapter = new TVShowsAdapter(mTVShowsDataSet, DiscoverFragment.this);

                                    if (sort >=3)
                                        sort = 0;
                                }
                                mRecyclerView.setAdapter(mAdapter);
                                getData(1);
                            }
                        })
                        .build();
                initFilterDialog(dialog);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onListEndReached() {
        if (!updating && page < totalPages)
            getData(page + 1);
    }

    private void initFilterDialog(MaterialDialog dialog) {
        View view = dialog.getView();

        AppCompatSpinner mediaTypeSpinner = view.findViewById(R.id.media_type_spinner);
        final AppCompatSpinner genreSpinner = view.findViewById(R.id.genre_spinner);
        final NumberPicker yearFromNumberPicker = view.findViewById(R.id.year_from_number_picker);
        final NumberPicker yearToNumberPicker = view.findViewById(R.id.year_to_number_picker);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.media_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mediaTypeSpinner.setAdapter(adapter);
        mediaTypeSpinner.setSelection(mediaType);
        mediaTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                CharSequence[] genres;
                if (position == 0) {
                    genres = new CharSequence[Application.getMovieGenreList().size() + 1];
                    genres[0] = getString(R.string.all);

                    for (int i = 1; i < genres.length; i++) {
                        genres[i] = Application.getMovieGenreList().get(i - 1).getName();
                    }
                } else {
                    genres = new CharSequence[Application.getGenres().size() + 1];
                    genres[0] = getString(R.string.all);

                    for (int i = 1; i < genres.length; i++) {
                        genres[i] = Application.getGenres().get(i - 1).getName();
                    }
                }

                ArrayAdapter<CharSequence> genreAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, genres);
                genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                genreSpinner.setAdapter(genreAdapter);
                genreSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        Calendar today = Calendar.getInstance();

        yearFromNumberPicker.setMinValue(1874);
        yearFromNumberPicker.setMaxValue(today.get(Calendar.YEAR) + 5);
        yearFromNumberPicker.setValue(yearFrom);
        yearFromNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                if (newVal > yearToNumberPicker.getValue()) {
                    yearToNumberPicker.setValue(newVal);
                }
            }
        });

        yearToNumberPicker.setMinValue(1874);
        yearToNumberPicker.setMaxValue(today.get(Calendar.YEAR) + 5);
        yearToNumberPicker.setValue(yearTo);
        yearToNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                if (newVal < yearFromNumberPicker.getValue()) {
                    yearFromNumberPicker.setValue(newVal);
                }
            }
        });

        CharSequence[] genres;
        if (mediaType == 0) {
            genres = new CharSequence[Application.getMovieGenreList().size() + 1];
            genres[0] = getString(R.string.all);

            for (int i = 1; i < genres.length; i++) {
                genres[i] = Application.getMovieGenreList().get(i - 1).getName();
            }
        } else {
            genres = new CharSequence[Application.getGenres().size() + 1];
            genres[0] = getString(R.string.all);

            for (int i = 1; i < genres.length; i++) {
                genres[i] = Application.getGenres().get(i - 1).getName();
            }
        }

        ArrayAdapter<CharSequence> genreAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, genres);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);
        genreSpinner.setSelection(genre);
        dialog.show();
    }

    private void getData(int page) {
        updating = true;
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(updating);

        int genreId = -1;
        if (mediaType == 0) {
            if (genre > 0)
                genreId = Application.getMovieGenreList().get(genre - 1).getId();
            TMDb.Discover.movieDiscover(page, sort, yearFrom, yearTo, genreId, mMoviesListener);
        } else {
            if (genre > 0)
                genreId = Application.getGenres().get(genre - 1).getId();
            TMDb.Discover.tvShowDiscover(page, sort, yearFrom, yearTo, genreId, mTVShowsListener);
        }
    }

}
