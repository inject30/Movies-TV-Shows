package com.free.top.tvshows.tube.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.free.top.tvshows.tube.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.top.tvshows.tube.api.TMDb;
import com.free.top.tvshows.tube.api.model.Cast;
import com.free.top.tvshows.tube.api.model.TVShow;
import com.free.top.tvshows.tube.api.model.Video;
import com.free.top.tvshows.tube.database.TVShowDAO;
import com.free.top.tvshows.tube.fragments.tvshows.InfoTab;
import com.free.top.tvshows.tube.fragments.tvshows.CastTab;
import com.free.top.tvshows.tube.fragments.tvshows.SeasonsTab;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TVShowActivity extends AppCompatActivity {

    private TVShow mTVShow;
    private ArrayList<Cast> mCasts;
    private ArrayList<Video> mVideos;
    private ArrayList<TVShow> mSimilar;
    private ArrayList<TVShow> mRecommendations;

    private TVShowDAO mTVShowDAO;

    private CoordinatorLayout mCoordinatorLayout;
    private ImageView mBackdrop;
    private TextView mGenres;
    private FloatingActionButton mWatchlistActionButton;

    private TMDb.TVShowDetailsListener mTVShowDetailsListener = new TMDb.TVShowDetailsListener() {
        @Override
        public void onResponse(TVShow tvShow, ArrayList<Cast> casts, ArrayList<Video> videos, ArrayList<TVShow> similar, ArrayList<TVShow> recommendations) {
            mTVShow = tvShow;
            mCasts = casts;
            mVideos = videos;
            mSimilar = similar;
            mRecommendations = recommendations;

            Picasso.with(TVShowActivity.this)
                    .load("http://image.tmdb.org/t/p/w1280" + mTVShow.getBackdropPath())
                    .into(mBackdrop);
            StringBuilder genresStr = new StringBuilder();
            if (mTVShow.getGenres() != null) {
                for (int i = 0; i < mTVShow.getGenres().length; i++) {
                    if (i != mTVShow.getGenres().length - 1)
                        genresStr.append(mTVShow.getGenres()[i].getName()).append(", ");
                    else
                        genresStr.append(mTVShow.getGenres()[i].getName());
                }
            }
            mGenres = findViewById(R.id.genres);
            mGenres.setText(genresStr.toString());

            if (mTVShowDAO.isInWatchlist(mTVShow))
                mTVShowDAO.updateTVShowData(mTVShow);

            Intent intent = new Intent("tv_show_details_received");
            intent.putExtra("tv_show", tvShow);
            intent.putExtra("casts", casts);
            intent.putExtra("videos", videos);
            intent.putExtra("similar", similar);
            intent.putExtra("recommendations", recommendations);
            LocalBroadcastManager.getInstance(TVShowActivity.this).sendBroadcast(intent);
        }

        @Override
        public void onError() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTVShowDAO = new TVShowDAO(this);

        Intent intent = getIntent();
        mTVShow = (TVShow) intent.getSerializableExtra("tv_show");

        getSupportActionBar().setTitle(mTVShow.getName());

        mCoordinatorLayout = findViewById(R.id.coordinator_layout);

        mBackdrop = findViewById(R.id.backdrop);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w1280" + mTVShow.getBackdropPath())
                .into(mBackdrop);

        ImageView poster = findViewById(R.id.poster);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w342" + mTVShow.getPosterPath())
                .placeholder(R.drawable.no_poster)
                .error(R.drawable.no_poster)
                .into(poster);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            poster.setTransitionName("poster_transition");

        TextView releaseDate = findViewById(R.id.release_date);
        if (mTVShow.getFirstAirDate() != null && mTVShow.getFirstAirDate().length() > 4)
            releaseDate.setText(mTVShow.getFirstAirDate().substring(0, 4));

        TextView title = findViewById(R.id.title);
        title.setText(mTVShow.getName());

        StringBuilder genresStr = new StringBuilder();
        if (mTVShow.getGenres() != null) {
            for (int i = 0; i < mTVShow.getGenres().length; i++) {
                if (mTVShow.getGenres()[i] != null) {
                    if (i != mTVShow.getGenres().length - 1)
                        genresStr.append(mTVShow.getGenres()[i].getName()).append(", ");
                    else
                        genresStr.append(mTVShow.getGenres()[i].getName());
                }
            }
        }
        mGenres = findViewById(R.id.genres);
        mGenres.setText(genresStr.toString());

        mWatchlistActionButton = findViewById(R.id.watchlist_action_button);
        if (mTVShowDAO.isInWatchlist(mTVShow))
            mWatchlistActionButton.setImageResource(R.drawable.ic_visibility_off_white_24dp);
        else
            mWatchlistActionButton.setImageResource(R.drawable.ic_visibility_white_24dp);

        mWatchlistActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTVShowDAO.isInWatchlist(mTVShow)) {
                    mTVShowDAO.removeTVShowFromWatchlist(mTVShow);
                    mWatchlistActionButton.setImageResource(R.drawable.ic_visibility_white_24dp);
                    Snackbar.make(mCoordinatorLayout, R.string.removed_from_watchlist, Snackbar.LENGTH_LONG).show();
                } else {
                    mTVShowDAO.addTVShowToWatchlist(mTVShow);
                    mWatchlistActionButton.setImageResource(R.drawable.ic_visibility_off_white_24dp);
                    Snackbar.make(mCoordinatorLayout, R.string.added_to_watchlist, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        TMDb.TV.getDetails(mTVShow.getId(), mTVShowDetailsListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, mTVShow.getName() + "\n" + "https://www.themoviedb.org/tv/" + mTVShow.getId());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                return true;
            case R.id.action_view_on_tmdb:
                Intent tmdbIntent = new Intent(Intent.ACTION_VIEW);
                tmdbIntent.setData(Uri.parse("https://www.themoviedb.org/tv/" + mTVShow.getId()));
                startActivity(tmdbIntent);
                return true;
            case R.id.action_view_on_imdb:
                Intent imdbIntent = new Intent(Intent.ACTION_VIEW);
                imdbIntent.setData(Uri.parse("http://www.imdb.com/title/" + mTVShow.getImdbId()));
                startActivity(imdbIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private class TabsPagerAdapter extends FragmentPagerAdapter {

        private String[] tabs = {
                getString(R.string.info),
                getString(R.string.cast),
                getString(R.string.seasons)
        };

        TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return InfoTab.newInstance(mTVShow, mVideos, mSimilar, mRecommendations);
                case 1:
                    return CastTab.newInstance(mCasts);
                case 2:
                    return SeasonsTab.newInstance(mTVShow);
                default:
                    return null;
            }
        }
    }
}
