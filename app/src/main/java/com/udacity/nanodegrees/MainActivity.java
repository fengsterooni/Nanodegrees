package com.udacity.nanodegrees;

import com.udacity.nanodegrees.data.DegreeLoader;
import com.udacity.nanodegrees.model.NanoDegree;
import com.udacity.nanodegrees.service.UpdaterService;
import com.udacity.nanodegrees.util.ImageLoaderHelper;
import com.udacity.nanodegrees.util.NetworkUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

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

        getSupportLoaderManager().initLoader(0, null, this);
        if (NetworkUtils.isNetworkAvailable(this))
            startService(new Intent(this, UpdaterService.class));
        else
            Snackbar.make(mRecyclerView, "Check your network connection", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mDegreeReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mDegreeReceiver);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return DegreeLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Adapter adapter = new Adapter(data);
        mRecyclerView.setAdapter(adapter);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }


    private class Adapter extends RecyclerView.Adapter<ViewHolder> {
        private Cursor mCursor;

        public Adapter(Cursor cursor) {
            mCursor = cursor;
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(DegreeLoader.Query._ID);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_degree, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            holder.name.setText(mCursor.getString(DegreeLoader.Query.NAME));

            holder.image.setImageUrl(
                    mCursor.getString(DegreeLoader.Query.IMAGE),
                    ImageLoaderHelper.getInstance(MainActivity.this).getImageLoader());
        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        public TextView name;
        @Bind(R.id.image)
        public DynamicHeightNetworkImageView image;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private BroadcastReceiver mDegreeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                // updateRefreshingUI();
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_LONG).show();

            }
        }
    };
}
