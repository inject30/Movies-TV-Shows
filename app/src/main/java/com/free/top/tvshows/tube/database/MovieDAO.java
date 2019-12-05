package com.free.top.tvshows.tube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.free.top.tvshows.tube.Application;
import com.free.top.tvshows.tube.api.model.Genre;
import com.free.top.tvshows.tube.api.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    private DatabaseHelper databaseHelper;

    public MovieDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<Movie> getMovies() {
        ArrayList<Movie> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.MOVIES_WATCHLIST_TABLE, null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex(DatabaseHelper.ID_COLUMN);
                int titleColumnIndex = cursor.getColumnIndex(DatabaseHelper.TITLE_COLUMN);
                int backdropPathColumnIndex = cursor.getColumnIndex(DatabaseHelper.BACKDROP_PATH_COLUMN);
                int posterPathColumnIndex = cursor.getColumnIndex(DatabaseHelper.POSTER_PATH_COLUMN);
                int releaseDateColumnIndex = cursor.getColumnIndex(DatabaseHelper.RELEASE_DATE_COLUMN);
                int genresColumnIndex = cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN);
                do {
                    Movie movie = new Movie();
                    movie.setId(cursor.getInt(idColumnIndex));
                    movie.setTitle(cursor.getString(titleColumnIndex));
                    movie.setBackdropPath(cursor.getString(backdropPathColumnIndex));
                    movie.setPosterPath(cursor.getString(posterPathColumnIndex));
                    movie.setReleaseDate(cursor.getString(releaseDateColumnIndex));

                    String genresStr = cursor.getString(genresColumnIndex);
                    String [] genresIds = genresStr.split(",");
                    Genre[] genres = new Genre[genresIds.length];

                    List<Genre> genresList = Application.getMovieGenreList();
                    for (int i = 0; i < genresIds.length; i++) {
                        int genreId = Integer.valueOf(genresIds[i]);
                        for (int j = 0; j < genresList.size(); j++) {
                            if (genresList.get(j).getId() == genreId) {
                                genres[i] = genresList.get(j);
                                break;
                            }
                        }
                    }
                    movie.setGenres(genres);
                    result.add(movie);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return result;
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }
    }

    public boolean isInWatchlist(Movie movie) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selection = DatabaseHelper.ID_COLUMN + "=?";
        String[] selectionArgs = {String.valueOf(movie.getId())};

        Cursor cursor = db.query(DatabaseHelper.MOVIES_WATCHLIST_TABLE, null, selection, selectionArgs, null, null, null);
        try {
            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
    }

    public boolean addMovieToWatchlist(Movie movie) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseHelper.ID_COLUMN, movie.getId());
        newValues.put(DatabaseHelper.TITLE_COLUMN, movie.getTitle());
        newValues.put(DatabaseHelper.BACKDROP_PATH_COLUMN, movie.getBackdropPath());
        newValues.put(DatabaseHelper.POSTER_PATH_COLUMN, movie.getPosterPath());
        newValues.put(DatabaseHelper.RELEASE_DATE_COLUMN, movie.getReleaseDate());

        StringBuilder genresStr = new StringBuilder();
        for (int i = 0; i < movie.getGenres().length; i++) {
            if (i < movie.getGenres().length - 1)
                genresStr.append(movie.getGenres()[i].getId()).append(",");
            else
                genresStr.append(movie.getGenres()[i].getId());
        }
        newValues.put(DatabaseHelper.GENRES_COLUMN, genresStr.toString());
        try {
            long result = db.insert(DatabaseHelper.MOVIES_WATCHLIST_TABLE, null, newValues);
            return result != -1;
        } finally {
            db.close();
        }
    }

    public boolean removeMovieFromWatchlist(Movie movie) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String selection = DatabaseHelper.ID_COLUMN + "=?";
        String[] selectionArgs = {String.valueOf(movie.getId())};
        try {
            int result = db.delete(DatabaseHelper.MOVIES_WATCHLIST_TABLE, selection, selectionArgs);
            return result > 0;
        } finally {
            db.close();
        }
    }

    public boolean updateMovieData(Movie movie) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String selection = DatabaseHelper.ID_COLUMN + "=?";
        String[] selectionArgs = {String.valueOf(movie.getId())};

        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseHelper.ID_COLUMN, movie.getId());
        newValues.put(DatabaseHelper.TITLE_COLUMN, movie.getTitle());
        newValues.put(DatabaseHelper.BACKDROP_PATH_COLUMN, movie.getBackdropPath());
        newValues.put(DatabaseHelper.POSTER_PATH_COLUMN, movie.getPosterPath());
        newValues.put(DatabaseHelper.RELEASE_DATE_COLUMN, movie.getReleaseDate());

        StringBuilder genresStr = new StringBuilder();
        for (int i = 0; i < movie.getGenres().length; i++) {
            if (i < movie.getGenres().length - 1)
                genresStr.append(movie.getGenres()[i].getId()).append(",");
            else
                genresStr.append(movie.getGenres()[i].getId());
        }
        newValues.put(DatabaseHelper.GENRES_COLUMN, genresStr.toString());
        try {
            long result = db.update(DatabaseHelper.MOVIES_WATCHLIST_TABLE, newValues, selection, selectionArgs);
            return result != -1;
        } finally {
            db.close();
        }
    }
}
