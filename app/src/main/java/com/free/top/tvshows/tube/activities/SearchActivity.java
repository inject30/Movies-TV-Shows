package com.free.top.tvshows.tube.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.free.top.tvshows.tube.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.free.top.tvshows.tube.fragments.search.MoviesSearchFragment;
import com.free.top.tvshows.tube.fragments.search.PeopleSearchFragment;
import com.free.top.tvshows.tube.fragments.search.TVShowsSearchFragment;
public class SearchActivity extends AppCompatActivity {

    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            getSupportActionBar().setTitle(mQuery);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

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

    private class TabsPagerAdapter extends FragmentPagerAdapter {
        private String[] tabs = {
                getString(R.string.movies),
                getString(R.string.tv_shows),
                getString(R.string.people)
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
                    return MoviesSearchFragment.newInstance(mQuery);
                case 1:
                    return TVShowsSearchFragment.newInstance(mQuery);
                case 2:
                    return PeopleSearchFragment.newInstance(mQuery);
                default:
                    return null;
            }
        }
    }
}
