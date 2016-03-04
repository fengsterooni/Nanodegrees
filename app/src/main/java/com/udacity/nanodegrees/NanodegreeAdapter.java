package com.udacity.nanodegrees;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NanodegreeAdapter extends RecyclerView.Adapter<NanodegreeAdapter.ViewHolder> {
    private List<NanoDegree> mDegrees;
    private Context mContext;

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

    public NanodegreeAdapter(List<NanoDegree> nanoDegrees) {
        this.mDegrees = nanoDegrees;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_degree, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NanoDegree degree = mDegrees.get(position);

        holder.name.setText("" + degree.getName());
        holder.image.setImageUrl(degree.getImage(),
                ImageLoaderHelper.getInstance(mContext).getImageLoader());
    }

    @Override
    public int getItemCount() {
        return mDegrees.size();
    }
}
