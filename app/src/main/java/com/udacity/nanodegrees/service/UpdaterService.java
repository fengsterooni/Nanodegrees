package com.udacity.nanodegrees.service;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.udacity.nanodegrees.data.DegreeContract;
import com.udacity.nanodegrees.model.NanoDegree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class UpdaterService extends Service {
    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.udacity.nanodegrees.intent.action.STATE_CHANGE";

    LocalBroadcastManager broadcastManager;
    String degreeString = "https://www.udacity.com/public-api/v0/courses?projection=internal";

    public UpdaterService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();
        final Uri degreeUri = DegreeContract.DegreeEntry.buildDegreeUri();

        cpo.add(ContentProviderOperation.newDelete(degreeUri).build());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(degreeString, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray degreesJSON = null;
                try {
                    degreesJSON = response.getJSONArray("degrees");

                    if (degreesJSON == null) throw new JSONException("Invalid JSON Array");
                    List<NanoDegree> list = NanoDegree.fromJSONArray(degreesJSON);

                    for (NanoDegree degree : list) {
                        ContentValues values = new ContentValues();
                        values.put(DegreeContract.DegreeEntry.COLUMN_NAME, degree.getName());
                        values.put(DegreeContract.DegreeEntry.COLUMN_IMAGE, degree.getImage());
                        cpo.add(ContentProviderOperation.newInsert(degreeUri).withValues(values).build());
                    }

                    getContentResolver().applyBatch(DegreeContract.CONTENT_AUTHORITY, cpo);
                    broadcastManager.sendBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}
