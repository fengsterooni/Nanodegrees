package com.udacity.nanodegrees.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{

    private final String LOG_TAG = DbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "nanodegree.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_DEGREE_TABLE = "CREATE TABLE " + DegreeContract.DegreeEntry.TABLE_NAME + " (" +
                DegreeContract.DegreeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DegreeContract.DegreeEntry.COLUMN_NAME + " TEXT NOT NULL," +
                DegreeContract.DegreeEntry.COLUMN_IMAGE + " TEXT NOT NULL," +

                " UNIQUE (" + DegreeContract.DegreeEntry.COLUMN_NAME + ") ON CONFLICT REPLACE);";

        Log.d(LOG_TAG, SQL_CREATE_DEGREE_TABLE);
        db.execSQL(SQL_CREATE_DEGREE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

