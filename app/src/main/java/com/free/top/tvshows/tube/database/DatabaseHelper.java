package com.free.top.tvshows.tube.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "watchlist.db";
    private static final int DATABASE_VERSION = 1;

    static final String ID_COLUMN = "id";
    static final String TITLE_COLUMN = "title";
    static final String BACKDROP_PATH_COLUMN = "backdrop_path";
    static final String POSTER_PATH_COLUMN = "poster_path";
    static final String RELEASE_DATE_COLUMN = "release_date";
    static final String GENRES_COLUMN = "genres";
    static final String FIRST_AIR_DATE_COLUMN = "first_air_date";
    static final String NAME_COLUMN = "name";

    static final String MOVIES_WATCHLIST_TABLE = "movies_watchlist";
    static final String TV_SHOWS_WATCHLIST_TABLE = "tv_shows_watchlist";
    static final String MOVIE_GENRES_TABLE = "movie_genres";
    static final String TV_GENRES_TABLE = "tv_genres";

    private static final String CREATE_TABLE_MOVIES_WATCHLIST = "CREATE TABLE " + MOVIES_WATCHLIST_TABLE + " (" + ID_COLUMN +
            " INTEGER PRIMARY KEY NOT NULL, " + TITLE_COLUMN + " TEXT, " + BACKDROP_PATH_COLUMN + " TEXT, " +
            RELEASE_DATE_COLUMN + " TEXT, " + GENRES_COLUMN + " TEXT, " + POSTER_PATH_COLUMN + " TEXT);";

    private static final String CREATE_TABLE_TV_SHOWS_WATCHLIST = "CREATE TABLE " + TV_SHOWS_WATCHLIST_TABLE + " (" + ID_COLUMN +
            " INTEGER PRIMARY KEY NOT NULL, " + NAME_COLUMN + " TEXT, " + BACKDROP_PATH_COLUMN + " TEXT, " +
            FIRST_AIR_DATE_COLUMN + " TEXT, " + GENRES_COLUMN + " TEXT, " + POSTER_PATH_COLUMN + " TEXT);";

    private static final String CREATE_MOVIE_GENRES_TABLE = "CREATE TABLE " + MOVIE_GENRES_TABLE + " (" + ID_COLUMN +
            " INTEGER PRIMARY KEY NOT NULL, " + NAME_COLUMN + " TEXT);";

    private static final String CREATE_TV_GENRES_TABLE = "CREATE TABLE " + TV_GENRES_TABLE + " (" + ID_COLUMN +
            " INTEGER PRIMARY KEY NOT NULL, " + NAME_COLUMN + " TEXT);";

    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIES_WATCHLIST);
        sqLiteDatabase.execSQL(CREATE_TABLE_TV_SHOWS_WATCHLIST);
        sqLiteDatabase.execSQL(CREATE_MOVIE_GENRES_TABLE);
        sqLiteDatabase.execSQL(CREATE_TV_GENRES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + MOVIES_WATCHLIST_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + TV_SHOWS_WATCHLIST_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + MOVIE_GENRES_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + TV_GENRES_TABLE);

        onCreate(sqLiteDatabase);
    }
}
