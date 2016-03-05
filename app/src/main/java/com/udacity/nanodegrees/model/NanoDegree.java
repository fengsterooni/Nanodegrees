package com.udacity.nanodegrees.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NanoDegree implements Parcelable {
    private String name;
    private String image;

    public NanoDegree(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NanoDegree(JSONObject json) {

        try {
            this.name = json.getString("title");
            this.image = json.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<NanoDegree> fromJSONArray(JSONArray array) {
        ArrayList<NanoDegree> result = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                result.add(new NanoDegree(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.image);
    }

    protected NanoDegree(Parcel in) {
        this.name = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<NanoDegree> CREATOR = new Parcelable.Creator<NanoDegree>() {
        public NanoDegree createFromParcel(Parcel source) {
            return new NanoDegree(source);
        }

        public NanoDegree[] newArray(int size) {
            return new NanoDegree[size];
        }
    };
}
