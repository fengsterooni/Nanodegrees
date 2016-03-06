package com.udacity.nanodegrees.service;

import com.udacity.nanodegrees.data.DegreeContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class UpdaterService extends IntentService {
    public final String LOG_TAG = UpdaterService.class.getSimpleName();
    private static final String TAG = "UpdaterService";
    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.udacity.nanodegrees.intent.action.STATE_CHANGE";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String NANODEGREE_BASE_URL =
                "https://www.udacity.com/public-api/v0/courses?projection=internal";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(NANODEGREE_BASE_URL);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            String degreesJsonStr = buffer.toString();
            parseDegreesFromJson(degreesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private void parseDegreesFromJson(String jsonStr)
            throws JSONException {

        final String DEGREES = "degrees";
        final String TITLE = "title";
        final String IMAGE = "image";

        try {
            JSONObject JSON = new JSONObject(jsonStr);
            JSONArray degreesJSON = JSON.getJSONArray(DEGREES);

            if (degreesJSON == null) throw new JSONException("Invalid JSON Array");

            Vector<ContentValues> cVVector = new Vector<>(degreesJSON.length());

            for (int i = 0; i < degreesJSON.length(); i++) {
                JSONObject degree = degreesJSON.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(DegreeContract.DegreeEntry.COLUMN_NAME, degree.getString(TITLE));
                values.put(DegreeContract.DegreeEntry.COLUMN_IMAGE, degree.getString(IMAGE));
                cVVector.add(values);
            }

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContentResolver().bulkInsert(DegreeContract.DegreeEntry.CONTENT_URI, cvArray);

                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE));
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
