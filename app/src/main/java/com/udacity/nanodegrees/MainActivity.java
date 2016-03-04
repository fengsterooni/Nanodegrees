package com.udacity.nanodegrees;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

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

        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(sglm);

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
                    List<NanoDegree> list = NanoDegree.fromJSONArray(degreesJSON);
                    for (NanoDegree degree : list) {
                        degrees.add(degree);
                    }

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
