package com.free.top.tvshows.tube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.free.top.tvshows.tube.Application;
import com.free.top.tvshows.tube.api.model.Genre;
import com.free.top.tvshows.tube.api.model.TVShow;

import java.util.ArrayList;
import java.util.List;

public class TVShowDAO {

    private DatabaseHelper databaseHelper;

    public TVShowDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<TVShow> getTVShows() {
        ArrayList<TVShow> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TV_SHOWS_WATCHLIST_TABLE, null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex(DatabaseHelper.ID_COLUMN);
                int nameColumnIndex = cursor.getColumnIndex(DatabaseHelper.NAME_COLUMN);
                int backdropPathColumnIndex = cursor.getColumnIndex(DatabaseHelper.BACKDROP_PATH_COLUMN);
                int posterPathColumnIndex = cursor.getColumnIndex(DatabaseHelper.POSTER_PATH_COLUMN);
                int firstAirDateColumnIndex = cursor.getColumnIndex(DatabaseHelper.FIRST_AIR_DATE_COLUMN);
                int genresColumnIndex = cursor.getColumnIndex(DatabaseHelper.GENRES_COLUMN);
                do {
                    TVShow tvShow = new TVShow();
                    tvShow.setId(cursor.getInt(idColumnIndex));
                    tvShow.setName(cursor.getString(nameColumnIndex));
                    tvShow.setBackdropPath(cursor.getString(backdropPathColumnIndex));
                    tvShow.setPosterPath(cursor.getString(posterPathColumnIndex));
                    tvShow.setFirstAirDate(cursor.getString(firstAirDateColumnIndex));

                    String genresStr = cursor.getString(genresColumnIndex);
                    String [] genresIds = genresStr.split(",");
                    Genre[] genres = new Genre[genresIds.length];

                    List<Genre> genresList = Application.getGenres();
                    for (int i = 0; i < genresIds.length; i++) {
                        int genreId = Integer.valueOf(genresIds[i]);
                        for (int j = 0; j < genresList.size(); j++) {
                            if (genresList.get(j).getId() == genreId) {
                                genres[i] = genresList.get(j);
                                break;
                            }
                        }
                    }
                    tvShow.setGenres(genres);
                    result.add(tvShow);
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

    public boolean isInWatchlist(TVShow tvShow) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selection = DatabaseHelper.ID_COLUMN + "=?";
        String[] selectionArgs = {String.valueOf(tvShow.getId())};

        Cursor cursor = db.query(DatabaseHelper.TV_SHOWS_WATCHLIST_TABLE, null, selection, selectionArgs, null, null, null);
        try {
            return cursor != null && cursor.moveToFirst();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
    }

    public boolean addTVShowToWatchlist(TVShow tvShow) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseHelper.ID_COLUMN, tvShow.getId());
        newValues.put(DatabaseHelper.NAME_COLUMN, tvShow.getName());
        newValues.put(DatabaseHelper.BACKDROP_PATH_COLUMN, tvShow.getBackdropPath());
        newValues.put(DatabaseHelper.POSTER_PATH_COLUMN, tvShow.getPosterPath());
        newValues.put(DatabaseHelper.FIRST_AIR_DATE_COLUMN, tvShow.getFirstAirDate());

        String genresStr = "";
        for (int i = 0; i < tvShow.getGenres().length; i++) {
            if (i < tvShow.getGenres().length - 1)
                genresStr += tvShow.getGenres()[i].getId() + ",";
            else
                genresStr += tvShow.getGenres()[i].getId();
        }
        newValues.put(DatabaseHelper.GENRES_COLUMN, genresStr);
        try {
            long result = db.insert(DatabaseHelper.TV_SHOWS_WATCHLIST_TABLE, null, newValues);
            return result != -1;
        } finally {
            db.close();
        }
    }

    public boolean removeTVShowFromWatchlist(TVShow tvShow) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String selection = DatabaseHelper.ID_COLUMN + "=?";
        String[] selectionArgs = {String.valueOf(tvShow.getId())};
        try {
            int result = db.delete(DatabaseHelper.TV_SHOWS_WATCHLIST_TABLE, selection, selectionArgs);
            return result > 0;
        } finally {
            db.close();
        }
    }

    public boolean updateTVShowData(TVShow tvShow) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String selection = DatabaseHelper.ID_COLUMN + "=?";
        String[] selectionArgs = {String.valueOf(tvShow.getId())};

        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseHelper.ID_COLUMN, tvShow.getId());
        newValues.put(DatabaseHelper.NAME_COLUMN, tvShow.getName());
        newValues.put(DatabaseHelper.BACKDROP_PATH_COLUMN, tvShow.getBackdropPath());
        newValues.put(DatabaseHelper.POSTER_PATH_COLUMN, tvShow.getPosterPath());
        newValues.put(DatabaseHelper.FIRST_AIR_DATE_COLUMN, tvShow.getFirstAirDate());

        String genresStr = "";
        for (int i = 0; i < tvShow.getGenres().length; i++) {
            if (i < tvShow.getGenres().length - 1)
                genresStr += tvShow.getGenres()[i].getId() + ",";
            else
                genresStr += tvShow.getGenres()[i].getId();
        }
        newValues.put(DatabaseHelper.GENRES_COLUMN, genresStr);
        try {
            long result = db.update(DatabaseHelper.TV_SHOWS_WATCHLIST_TABLE, newValues, selection, selectionArgs);
            return result != -1;
        } finally {
            db.close();
        }
    }
}
