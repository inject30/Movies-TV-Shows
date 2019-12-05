package com.free.top.tvshows.tube.activities;

import android.content.Intent;
import android.net.Uri;
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
import com.free.top.tvshows.tube.api.model.Movie;
import com.free.top.tvshows.tube.api.model.Video;
import com.free.top.tvshows.tube.database.MovieDAO;
import com.free.top.tvshows.tube.fragments.movies.CastTab;
import com.free.top.tvshows.tube.fragments.movies.InfoTab;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {

    private Movie mMovie;
    private ArrayList<Cast> mCasts;
    private ArrayList<Video> mVideos;
    private ArrayList<Movie> mSimilarMovies;
    private ArrayList<Movie> mRecommendations;

    private MovieDAO mMovieDAO;

    private CoordinatorLayout mCoordinatorLayout;
    private ImageView mBackdrop;
    private TextView mGenres;
    private FloatingActionButton mWatchlistActionButton;

    private TMDb.MovieDetailsListener movieDetailsListener = new TMDb.MovieDetailsListener() {
        @Override
        public void onResponse(Movie movie, ArrayList<Cast> casts, ArrayList<Video> videos, ArrayList<Movie> similar, ArrayList<Movie> recommendations) {
            if (mMovie.getBackdropPath() == null) {
                Picasso.with(MovieActivity.this)
                        .load("http://image.tmdb.org/t/p/w1280" + movie.getBackdropPath())
                        .into(mBackdrop);
            }
            mMovie = movie;
            mCasts = casts;
            mVideos = videos;
            mSimilarMovies = similar;
            mRecommendations = recommendations;

            if (mMovieDAO.isInWatchlist(mMovie))
                mMovieDAO.updateMovieData(mMovie);

            Intent intent = new Intent("movie_details_received");
            intent.putExtra("movie", movie);
            intent.putExtra("casts", casts);
            intent.putExtra("videos", videos);
            intent.putExtra("similar", similar);
            intent.putExtra("recommendations", recommendations);

            StringBuilder genresStr = new StringBuilder();
            if (mMovie.getGenres() != null) {
                for (int i = 0; i < mMovie.getGenres().length; i++) {
                    if (i != mMovie.getGenres().length - 1)
                        genresStr.append(mMovie.getGenres()[i].getName()).append(", ");
                    else
                        genresStr.append(mMovie.getGenres()[i].getName());
                }
            }
            mGenres.setText(genresStr.toString());

            LocalBroadcastManager.getInstance(MovieActivity.this).sendBroadcast(intent);
            movieDetailsListener = null;
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

        mMovieDAO = new MovieDAO(this);

        Intent intent = getIntent();
        mMovie = (Movie) intent.getSerializableExtra("movie");

        getSupportActionBar().setTitle(mMovie.getTitle());

        mCoordinatorLayout = findViewById(R.id.coordinator_layout);

        mBackdrop = findViewById(R.id.backdrop);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w1280"+mMovie.getBackdropPath())
                .into(mBackdrop);

        ImageView poster = findViewById(R.id.poster);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w342"+mMovie.getPosterPath())
                .placeholder(R.drawable.no_poster)
                .error(R.drawable.no_poster)
                .into(poster);

        TextView releaseDate = findViewById(R.id.release_date);
        if (mMovie.getReleaseDate() != null && mMovie.getReleaseDate().length()>4)
            releaseDate.setText(mMovie.getReleaseDate().substring(0, 4));

        TextView title = findViewById(R.id.title);
        title.setText(mMovie.getTitle());

        StringBuilder genresStr = new StringBuilder();
        if (mMovie.getGenres() != null) {
            for (int i = 0; i < mMovie.getGenres().length; i++) {
                if (mMovie.getGenres()[i] != null) {
                    if (i != mMovie.getGenres().length - 1)
                        genresStr.append(mMovie.getGenres()[i].getName()).append(", ");
                    else
                        genresStr.append(mMovie.getGenres()[i].getName());
                }
            }
        }
        mGenres = findViewById(R.id.genres);
        mGenres.setText(genresStr.toString());

        mWatchlistActionButton = findViewById(R.id.watchlist_action_button);
        if (mMovieDAO.isInWatchlist(mMovie))
            mWatchlistActionButton.setImageResource(R.drawable.ic_visibility_off_white_24dp);
        else
            mWatchlistActionButton.setImageResource(R.drawable.ic_visibility_white_24dp);


        mWatchlistActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovieDAO.isInWatchlist(mMovie)) {
                    mMovieDAO.removeMovieFromWatchlist(mMovie);
                    mWatchlistActionButton.setImageResource(R.drawable.ic_visibility_white_24dp);
                    Snackbar.make(mCoordinatorLayout, R.string.removed_from_watchlist, Snackbar.LENGTH_LONG).show();
                } else {
                    mMovieDAO.addMovieToWatchlist(mMovie);
                    mWatchlistActionButton.setImageResource(R.drawable.ic_visibility_off_white_24dp);
                    Snackbar.make(mCoordinatorLayout, R.string.added_to_watchlist, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        TMDb.Movies.getDetails(mMovie.getId(), movieDetailsListener);

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
                shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle() + "\n" + "https://www.themoviedb.org/movie/" + mMovie.getId());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                return true;
            case R.id.action_view_on_tmdb:
                Intent tmdbIntent = new Intent(Intent.ACTION_VIEW);
                tmdbIntent.setData(Uri.parse("https://www.themoviedb.org/movie/" + mMovie.getId()));
                startActivity(tmdbIntent);
                return true;
            case R.id.action_view_on_imdb:
                Intent imdbIntent = new Intent(Intent.ACTION_VIEW);
                imdbIntent.setData(Uri.parse("http://www.imdb.com/title/" + mMovie.getImdbId()));
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
                    return InfoTab.newInstance(mMovie, mVideos, mSimilarMovies, mRecommendations);
                case 1:
                    return CastTab.newInstance(mCasts);
                default:
                    return null;
            }
        }
    }
}
