package com.free.top.tvshows.tube;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.free.top.tvshows.tube.api.TMDb;
import com.free.top.tvshows.tube.api.model.Genre;
import com.free.top.tvshows.tube.database.GenreDAO;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

public class Application extends android.app.Application {

    private static Context context;

    private static RequestQueue requestQueue;
    private static List<Genre> genres = new ArrayList<>();
    private static List<Genre> movieGenres;
    private static List<Genre> tvGenres;

    public void onCreate() {
        super.onCreate();

        final GenreDAO genreDAO = new GenreDAO(this);

        context = this;
        requestQueue = Volley.newRequestQueue(this);
        JodaTimeAndroid.init(this);

        movieGenres = genreDAO.getMovieGenres();
        tvGenres = genreDAO.getTVGenres();

        TMDb.Genres.getMovieList(new TMDb.Listener<Genre>() {
            @Override
            public void onResponse(List<Genre> data, int page, int totalPages) {
                movieGenres = data;

                if (!genres.isEmpty())
                    genres.clear();
                genres.addAll(movieGenres);
                genres.addAll(tvGenres);
            }

            @Override
            public void onError() {
            }
        });

        TMDb.Genres.getTvList(new TMDb.Listener<Genre>() {
            @Override
            public void onResponse(List<Genre> data, int page, int totalPages) {
                tvGenres = data;

                genres.clear();
                genres.addAll(movieGenres);
                genres.addAll(tvGenres);

            }

            @Override
            public void onError() {}
        });
    }

    public static Context getContext() {
        return context;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static List<Genre> getMovieGenreList() {
        return movieGenres;
    }

    public static List<Genre> getGenres() {
        return genres;
    }
}
