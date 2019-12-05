package com.free.top.tvshows.tube.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.free.top.tvshows.tube.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.free.top.tvshows.tube.api.TMDb;
import com.free.top.tvshows.tube.api.model.Person;
import com.free.top.tvshows.tube.api.model.CastCredit;
import com.free.top.tvshows.tube.api.model.CrewCredit;
import com.free.top.tvshows.tube.api.model.TVCastCredit;
import com.free.top.tvshows.tube.api.model.TVCrewCredit;
import com.free.top.tvshows.tube.fragments.people.InfoTab;
import com.free.top.tvshows.tube.fragments.people.MovieCreditsTab;
import com.free.top.tvshows.tube.fragments.people.TVCreditsTab;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

public class PersonActivity extends AppCompatActivity {

    private Person mPerson;
    private ArrayList<String> mImages;
    private ArrayList<String> mTaggedImages;
    private ArrayList<CastCredit> mMovieCast;
    private ArrayList<CrewCredit> mMovieCrew;
    private ArrayList<TVCastCredit> mTVCast;
    private ArrayList<TVCrewCredit> mTVCrew;

    private TextView mAge;
    private ImageView mFacebookButton;
    private ImageView mInstagramButton;
    private ImageView mTwitterButton;

    private CirclePageIndicator mCirclePageIndicator;

    private ImageSliderAdapter mImageSliderAdapter;

    private TMDb.ActorDetailsListener mActorDetailsListener = new TMDb.ActorDetailsListener() {
        @Override
        public void onResponse(Person person, ArrayList<String> images, ArrayList<String> taggedImages, ArrayList<CastCredit> movieCast, ArrayList<CrewCredit> movieCrew, ArrayList<TVCastCredit> tvCast, ArrayList<TVCrewCredit> tvCrew) {
            person.setKnownFor(mPerson.getKnownFor());
            mPerson = person;
            mImages = images;
            mTaggedImages = taggedImages;
            mMovieCast = movieCast;
            mMovieCrew = movieCrew;
            mTVCast = tvCast;
            mTVCrew = tvCrew;

            if (mPerson.getBirthday() != null && mPerson.getBirthday().length() == 10) {
                DateTime bDate = DateTimeFormat.forPattern("yyyy-mm-dd").parseDateTime(mPerson.getBirthday());
                DateTime dDate;
                if (mPerson.getDeathday() != null && mPerson.getDeathday().length() == 10)
                    dDate = DateTimeFormat.forPattern("yyyy-mm-dd").parseDateTime(mPerson.getDeathday());
                else
                    dDate = new DateTime();
                Period period = new Interval(bDate, dDate).toPeriod();
                mAge.setText(getResources().getQuantityString(R.plurals.years_old, period.getYears(), period.getYears()));
            }

            if (mPerson.getFacebookId() != null && !mPerson.getFacebookId().isEmpty())
                mFacebookButton.setVisibility(View.VISIBLE);
            if (mPerson.getInstagramId() != null && !mPerson.getInstagramId().isEmpty())
                mInstagramButton.setVisibility(View.VISIBLE);
            if (mPerson.getTwitterId() != null && !mPerson.getTwitterId().isEmpty())
                mTwitterButton.setVisibility(View.VISIBLE);

            mImageSliderAdapter.notifyDataSetChanged();
            mCirclePageIndicator.notifyDataSetChanged();

            Intent intent = new Intent("person_details_received");
            intent.putExtra("person", person);
            intent.putExtra("images", images);
            intent.putExtra("movie_cast", movieCast);
            intent.putExtra("movie_crew", movieCrew);
            intent.putExtra("tv_cast", tvCast);
            intent.putExtra("tv_crew", tvCrew);
            LocalBroadcastManager.getInstance(PersonActivity.this).sendBroadcast(intent);
        }

        @Override
        public void onError() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mPerson = (Person) intent.getSerializableExtra("person");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mPerson.getName());


        ViewPager imageSlider = findViewById(R.id.image_slider_view_pager);
        mImageSliderAdapter = new ImageSliderAdapter();
        imageSlider.setAdapter(mImageSliderAdapter);
        mCirclePageIndicator = findViewById(R.id.circle_page_indicator);
        mCirclePageIndicator.setViewPager(imageSlider);

        ImageView profile = findViewById(R.id.profile);

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w185" + mPerson.getProfilePath())
                .placeholder(R.drawable.no_profile)
                .error(R.drawable.no_profile)
                .into(profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            profile.setTransitionName("profile_transition");

        TextView name = findViewById(R.id.name);
        name.setText(mPerson.getName());

        mAge = findViewById(R.id.age);
        if (mPerson.getBirthday() != null && mPerson.getBirthday().length() == 10) {
            DateTime bDate = DateTimeFormat.forPattern("yyyy-mm-dd").parseDateTime(mPerson.getBirthday());
            DateTime dDate;
            if (mPerson.getDeathday() != null && mPerson.getDeathday().length() == 10)
                dDate = DateTimeFormat.forPattern("yyyy-mm-dd").parseDateTime(mPerson.getDeathday());
            else
                dDate = new DateTime();
            Period period = new Interval(bDate, dDate).toPeriod();
            mAge.setText(getResources().getQuantityString(R.plurals.years_old, period.getYears(), period.getYears()));
        }

        mFacebookButton = findViewById(R.id.facebook);
        mFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/" + mPerson.getFacebookId()));
                startActivity(intent);
            }
        });
        if (mPerson.getFacebookId() != null && !mPerson.getFacebookId().isEmpty())
            mFacebookButton.setVisibility(View.VISIBLE);
        mInstagramButton = findViewById(R.id.instagram);
        mInstagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.instagram.com/_u/" + mPerson.getInstagramId()));
                startActivity(intent);
            }
        });
        if (mPerson.getInstagramId() != null && !mPerson.getInstagramId().isEmpty())
            mInstagramButton.setVisibility(View.VISIBLE);
        mTwitterButton = findViewById(R.id.twitter);
        mTwitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://twitter.com/" + mPerson.getTwitterId()));
                startActivity(intent);
            }
        });
        if (mPerson.getTwitterId() != null && !mPerson.getTwitterId().isEmpty())
            mTwitterButton.setVisibility(View.VISIBLE);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        TMDb.People.getDetails(mPerson.getId(), mActorDetailsListener);
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
                getString(R.string.info),
                getString(R.string.movies),
                getString(R.string.tv_shows)
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
                    return InfoTab.newInstance(mPerson, mImages);
                case 1:
                    return MovieCreditsTab.newInstance(mMovieCast, mMovieCrew);
                case 2:
                    return TVCreditsTab.newInstance(mTVCast, mTVCrew);
                default:
                    return null;
            }
        }
    }

    private class ImageSliderAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mTaggedImages != null)
                return mTaggedImages.size();
            else
                return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = getLayoutInflater().inflate(R.layout.image_slider_item, container, false);

            ImageView imageView =itemView.findViewById(R.id.image_view);

            Picasso.with(PersonActivity.this)
                    .load("http://image.tmdb.org/t/p/w1280" + mTaggedImages.get(position))
                    .into(imageView);

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
