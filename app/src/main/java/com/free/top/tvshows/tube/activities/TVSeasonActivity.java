package com.free.top.tvshows.tube.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.adapters.TVEpisodesAdapter;
import com.free.top.tvshows.tube.api.TMDb;
import com.free.top.tvshows.tube.api.model.TVSeason;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.squareup.picasso.Picasso;

public class TVSeasonActivity extends AppCompatActivity {

    private TVSeason mTVSeason;

    private RecyclerView mRecyclerView;

    private TMDb.TVSeasonDetailsListener mTVSeasonDetailsListener = new TMDb.TVSeasonDetailsListener() {
        @Override
        public void onResponse(TVSeason tvSeason) {
            mTVSeason = tvSeason;
            mRecyclerView.setAdapter(new TVEpisodesAdapter(mTVSeason));
        }

        @Override
        public void onError() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        TVShow TVShow = (com.free.top.tvshows.tube.api.model.TVShow) intent.getSerializableExtra("tv_show");
        mTVSeason = (TVSeason) intent.getSerializableExtra("tv_season");

        getSupportActionBar().setTitle(TVShow.getName());
        getSupportActionBar().setSubtitle(mTVSeason.getSeasonNumber() + " " + getString(R.string.season));

        ImageView backdrop = findViewById(R.id.backdrop);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w1280" + TVShow.getBackdropPath())
                .into(backdrop);

        ImageView poster = findViewById(R.id.poster);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w342" + mTVSeason.getPosterPath())
                .placeholder(R.drawable.no_poster)
                .error(R.drawable.no_poster)
                .into(poster);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            poster.setTransitionName("poster_transition");

        TextView releaseDate = findViewById(R.id.release_date);
        releaseDate.setText(mTVSeason.getAirDate().substring(0, 4));

        TextView title = findViewById(R.id.title);
        title.setText(TVShow.getName());

        TextView seasonNumber = findViewById(R.id.season_number);
        seasonNumber.setText(mTVSeason.getSeasonNumber() + " " + getString(R.string.season));


        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(false);
        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        TMDb.TVSeasons.getDetails(TVShow.getId(), mTVSeason.getSeasonNumber(), mTVSeasonDetailsListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
