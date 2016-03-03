package com.udacity.nanodegrees;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NanodegreeAdapter extends ArrayAdapter<NanoDegree> {
    public NanodegreeAdapter(Context context, List<NanoDegree> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final NanoDegree degree = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_degree, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(degree.getName());
        String imageString = degree.getImage();
        if (imageString != null)
            Picasso.with(getContext()).load(degree.getImage()).resize(600, 600).into(viewHolder.image);

        return convertView;
    }

    private static class ViewHolder {
        TextView name;
        ImageView image;
    }
}
