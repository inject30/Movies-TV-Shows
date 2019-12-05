package com.free.top.tvshows.tube.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.fragments.DiscoverFragment;
import com.free.top.tvshows.tube.fragments.movies.MoviesFragment;
import com.free.top.tvshows.tube.fragments.people.PopularPeopleFragment;
import com.free.top.tvshows.tube.fragments.tvshows.TVShowsFragment;
import com.free.top.tvshows.tube.fragments.watchlist.WatchlistFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    private static final String DRAWER_ITEM_SELECTED = "drawer_item_selected";

    private static final String TAG_MOVIE_FRAGMENT = "movie_fragment";
    private static final String TAG_TV_SHOW_FRAGMENT = "tv_show_fragment";
    private static final String TAG_POPULAR_PEOPLE_FRAGMENT = "popular_people_fragment";
    private static final String TAG_DISCOVER_FRAGMENT = "discover_fragment";
    private static final String TAG_WATCHLIST_FRAGMENT = "watchlist_fragment";

    private Drawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int drawerSelectedItem = 1;
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MoviesFragment(), TAG_MOVIE_FRAGMENT)
                    .commit();
        } else {
            drawerSelectedItem = savedInstanceState.getInt(DRAWER_ITEM_SELECTED, 1);
        }

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .withSelectionListEnabled(false)
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                //.withAccountHeader(accountHeader)
                .withHeader(R.layout.header_layout)
                .withShowDrawerOnFirstLaunch(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.movies).withIcon(GoogleMaterial.Icon.gmd_movie),
                        new PrimaryDrawerItem().withName(R.string.tv_shows).withIcon(GoogleMaterial.Icon.gmd_live_tv),
                        new PrimaryDrawerItem().withName(R.string.popular_people).withIcon(GoogleMaterial.Icon.gmd_people),
                        new PrimaryDrawerItem().withName(R.string.discover).withIcon(GoogleMaterial.Icon.gmd_pageview),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.watchlist).withIcon(GoogleMaterial.Icon.gmd_visibility)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 1:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new MoviesFragment(), TAG_MOVIE_FRAGMENT)
                                        .commit();
                                return false;
                            case 2:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new TVShowsFragment(), TAG_TV_SHOW_FRAGMENT)
                                        .commit();
                                return false;
                            case 3:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new PopularPeopleFragment(), TAG_POPULAR_PEOPLE_FRAGMENT)
                                        .commit();
                                return false;
                            case 4:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new DiscoverFragment(), TAG_DISCOVER_FRAGMENT)
                                        .commit();
                                return false;
                            case 6:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new WatchlistFragment(), TAG_WATCHLIST_FRAGMENT)
                                        .commit();
                                return false;
                            default:
                                return true;
                        }
                    }
                }).withSelectedItemByPosition(drawerSelectedItem).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));

        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DRAWER_ITEM_SELECTED, mDrawer.getCurrentSelectedPosition());
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
