package com.udacity.nanodegrees;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.udacity.nanodegrees.intent.action.STATE_CHANGE";

    String degreeString = "https://www.udacity.com/public-api/v0/courses?projection=internal";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            return;
        }

        final ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

        final Uri degreeUri = DegreeContract.DegreeEntry.buildDegreeUri();

        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(degreeUri).build());

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(degreeString, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray degrees = null;

                try {
                    degrees = response.getJSONArray("degrees");
                    if (degrees == null) throw new JSONException("Invalid JSON Array");
                    for (int i = 0; i < degrees.length(); i++) {
                        ContentValues values = new ContentValues();
                        JSONObject object = degrees.getJSONObject(i);
                        values.put(DegreeContract.DegreeEntry.COLUMN_NAME, object.getString("name" ));
                        values.put(DegreeContract.DegreeEntry.COLUMN_IMAGE, object.getString("image" ));
                        cpo.add(ContentProviderOperation.newInsert(degreeUri).withValues(values).build());
                    }

                    getContentResolver().applyBatch(DegreeContract.CONTENT_AUTHORITY, cpo);

                    sendStickyBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });

        // sendStickyBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE));
    }
}
