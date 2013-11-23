package com.hasgeek.funnel.misc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "hsgk";
    public static final int DATABASE_VERSION = 1;

    public static final String SESSIONS_TABLE = "sessions";
    public static final String ROOMS_TABLE = "rooms";
    public static final String VENUES_TABLE = "venues";

    private static DBManager mInstance = null;

    private static final String CREATE_TABLE_SESSIONS = "CREATE TABLE " + SESSIONS_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id TEXT NOT NULL UNIQUE, " +
            "title TEXT NOT NULL, " +
            "start TEXT NOT NULL, " +
            "end TEXT NOT NULL, " +
            "is_break BOOLEAN NOT NULL, " +
            "slot_ist TEXT NOT NULL, " +
            "date_ist TEXT NOT NULL, " +
            "room TEXT, " +
            "speaker TEXT, " +
            "section TEXT, " +
            "level TEXT, " +
            "description TEXT, " +
            "url TEXT, " +
            "bookmarked BOOLEAN" +
            ");";

    private static final String CREATE_TABLE_ROOMS = "CREATE TABLE " + ROOMS_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL UNIQUE, " +
            "title TEXT NOT NULL, " +
            "bgcolor TEXT NOT NULL, " +
            "description TEXT, " +
            "venue TEXT NOT NULL " +
            ");";

    private static final String CREATE_TABLE_VENUES = "CREATE TABLE " + VENUES_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL UNIQUE, " +
            "title TEXT NOT NULL, " +
            "address1 TEXT, " +
            "address2 TEXT, " +
            "city TEXT, " +
            "state TEXT, " +
            "country TEXT, " +
            "postcode NUMBER, " +
            "latitude FLOAT, " +
            "longitude FLOAT, " +
            "description TEXT " +
            ");";


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static DBManager getInstance(Context c) {
        if (mInstance == null) {
            mInstance = new DBManager(c.getApplicationContext());
        }

        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SESSIONS);
        db.execSQL(CREATE_TABLE_ROOMS);
        db.execSQL(CREATE_TABLE_VENUES);
        Log.w("Hasgeek", "Created sessions table.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Hasgeek", "Database needs an update from version " + String.valueOf(oldVersion) + " to " + String.valueOf(newVersion));
    }
}

