package com.free.top.tvshows.tube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.free.top.tvshows.tube.api.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreDAO {

    private DatabaseHelper databaseHelper;

    public GenreDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<Genre> getMovieGenres() {
        List<Genre> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.MOVIE_GENRES_TABLE, null, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex(DatabaseHelper.ID_COLUMN);
                int nameColumnIndex = cursor.getColumnIndex(DatabaseHelper.NAME_COLUMN);
                do {
                    Genre genre = new Genre();
                    genre.setId(cursor.getInt(idColumnIndex));
                    genre.setName(cursor.getString(nameColumnIndex));

                    result.add(genre);
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

    public List<Genre> getTVGenres() {
        List<Genre> result = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TV_GENRES_TABLE, null, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex(DatabaseHelper.ID_COLUMN);
                int nameColumnIndex = cursor.getColumnIndex(DatabaseHelper.NAME_COLUMN);
                do {
                    Genre genre = new Genre();
                    genre.setId(cursor.getInt(idColumnIndex));
                    genre.setName(cursor.getString(nameColumnIndex));

                    result.add(genre);
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
}
