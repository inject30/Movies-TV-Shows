package com.free.top.tvshows.tube.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.free.top.tvshows.tube.R;
import com.free.top.tvshows.tube.api.model.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageGalleryActivity extends AppCompatActivity {

    private ArrayList<String> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mImages = intent.getStringArrayListExtra("images");
        int position = intent.getIntExtra("position", 0);
        Person person = (Person) intent.getSerializableExtra("person");

        getSupportActionBar().setTitle(person.getName());
        getSupportActionBar().setSubtitle((position + 1) + " " + getString(R.string.of) + " " + mImages.size());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new ImageGalleryAdapter());
        viewPager.setCurrentItem(position, false);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSubtitle((position + 1) + " " + getString(R.string.of) + " " + mImages.size());
            }
        });
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

    private class ImageGalleryAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImages.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.image_gallery_item, container, false);

            ImageView imagePreview = view.findViewById(R.id.image_preview);

            Picasso.with(imagePreview.getContext())
                    .load("http://image.tmdb.org/t/p/original" + mImages.get(position))
                    .into(imagePreview);

            container.addView(view);

            return view;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}