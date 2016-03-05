package com.udacity.nanodegrees;

import com.udacity.nanodegrees.model.NanoDegree;
import com.udacity.nanodegrees.util.ImageLoaderHelper;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static Context mContext;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.image)
        DynamicHeightNetworkImageView image;

        public ViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            ButterKnife.bind(this, view);
        }

        public void setName(CharSequence text) {
            name.setText(text);
        }

        public void setImage(String imageUrl) {
            image.setImageUrl(imageUrl,
                    ImageLoaderHelper.getInstance(mContext).getImageLoader());
        }

        public void setImageResource(int resourceID) {
            image.setImageResource(resourceID);
        }
    }

    public NanodegreeAdapter(List<NanoDegree> degrees) {
        this.mDegrees = degrees;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_degree, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NanoDegree degree = mDegrees.get(position);

        holder.setName(degree.getName());
        if (degree.getImage() != null)
            holder.setImage(degree.getImage());
        else
            holder.setImageResource(R.mipmap.udacity);
    }

    @Override
    public int getItemCount() {
        return mDegrees.size();
    }
}
