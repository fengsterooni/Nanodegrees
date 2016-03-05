package com.udacity.nanodegrees.data;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Helper for loading a list of articles or a single article.
 */
public class DegreeLoader extends CursorLoader {
    public static DegreeLoader newAllArticlesInstance(Context context) {
        return new DegreeLoader(context, DegreeContract.DegreeEntry.buildDegreeUri());
    }

    public static DegreeLoader newInstanceForItemId(Context context, long id) {
        return new DegreeLoader(context, DegreeContract.DegreeEntry.buildDegreeUri(id));
    }

    private DegreeLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, null/*DegreeContract.DegreeEntry.DEFAULT_SORT*/);
    }

    public interface Query {
        String[] PROJECTION = {
                DegreeContract.DegreeEntry._ID,
                DegreeContract.DegreeEntry.COLUMN_NAME,
                DegreeContract.DegreeEntry.COLUMN_IMAGE,
        };

        int _ID = 0;
        int NAME = 1;
        int IMAGE = 2;
    }
}
