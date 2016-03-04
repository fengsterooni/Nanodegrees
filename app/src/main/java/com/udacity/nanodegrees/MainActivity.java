package com.udacity.nanodegrees;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ArrayList<NanoDegree> degrees;
    private NanodegreeAdapter aDegrees;
    @Bind(R.id.lvDegrees)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        degrees = new ArrayList<>();
        aDegrees = new NanodegreeAdapter(degrees);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(aDegrees);

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
                    // aDegrees.addAll(NanoDegree.fromJSONArray(degreesJSON));
                    degrees = NanoDegree.fromJSONArray(degreesJSON);
                    aDegrees.notifyDataSetChanged();
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
