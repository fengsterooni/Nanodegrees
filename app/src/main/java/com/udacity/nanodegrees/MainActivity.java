package com.udacity.nanodegrees;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ArrayList<NanoDegree> degrees;
    private NanodegreeAdapter aDegrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        degrees = new ArrayList<>();
        aDegrees = new NanodegreeAdapter(this, degrees);

        ListView lvDegrees = (ListView) findViewById(R.id.lvDegrees);
        lvDegrees.setAdapter(aDegrees);

        fetchDegrees();
    }

    private void fetchDegrees() {
        String degreeString = "https://www.udacity.com/public-api/v0/courses?projection=internal";
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(degreeString, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray degreesJSON = null;

                try {
                    degreesJSON = response.getJSONArray("degrees");
                    degrees.clear();
                    aDegrees.addAll(NanoDegree.fromJSONArray(degreesJSON));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });

    }

}
