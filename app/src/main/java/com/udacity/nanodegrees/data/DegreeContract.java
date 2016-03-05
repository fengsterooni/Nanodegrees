package com.udacity.nanodegrees.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DegreeContract {
	public static final String CONTENT_AUTHORITY = "com.udacity.nanodegrees";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

	public static final String PATH_DEGREE = "degrees";

	public static final class DegreeEntry implements BaseColumns {
		public static final Uri CONTENT_URI =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEGREE).build();

		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEGREE;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEGREE;

		// Table name
		public static final String TABLE_NAME = "degrees";

		// Columns
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_IMAGE = "image";

		public static Uri buildDegreeUri() {
			return CONTENT_URI;
		}
		public static Uri buildDegreeUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}
}
