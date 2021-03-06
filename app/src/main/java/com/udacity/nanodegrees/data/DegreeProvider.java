package com.udacity.nanodegrees.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DegreeProvider extends ContentProvider {

    private final String LOG_TAG = DegreeProvider.class.getSimpleName();
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int DEGREE_ID = 100;
    private static final int DEGREE = 101;

    private DbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DegreeContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DegreeContract.PATH_DEGREE + "/#", DEGREE_ID);
        matcher.addURI(authority, DegreeContract.PATH_DEGREE, DEGREE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case DEGREE:
                retCursor = dbHelper.getReadableDatabase().query(
                        DegreeContract.DegreeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case DEGREE_ID:
                retCursor = dbHelper.getReadableDatabase().query(
                        DegreeContract.DegreeEntry.TABLE_NAME,
                        projection,
                        DegreeContract.DegreeEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case DEGREE_ID:
                return DegreeContract.DegreeEntry.CONTENT_ITEM_TYPE;
            case DEGREE:
                return DegreeContract.DegreeEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case DEGREE: {
                long _id = db.insert(DegreeContract.DegreeEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = DegreeContract.DegreeEntry.buildDegreeUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case DEGREE:
                rowsDeleted = db.delete(
                        DegreeContract.DegreeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DEGREE_ID:
                rowsDeleted = db.delete(
                        DegreeContract.DegreeEntry.TABLE_NAME,
                        DegreeContract.DegreeEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case DEGREE:
                rowsUpdated = db.update(DegreeContract.DegreeEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case DEGREE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(DegreeContract.DegreeEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
             default:
                return super.bulkInsert(uri, values);
        }
    }
}
