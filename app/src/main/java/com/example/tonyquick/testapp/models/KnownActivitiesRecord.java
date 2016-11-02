package com.example.tonyquick.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tony Quick on 11/04/2016.
 */

//method to define gps data record representing individual database record
//parcelable implmentation so it can be passed between activities

public class KnownActivitiesRecord implements Parcelable {
    private long _id;
    private String category;
    private String name;


    public KnownActivitiesRecord(long _id, String category, String name) {
        this._id = _id;
        this.category = category;
        this.name = name;
    }

    public KnownActivitiesRecord(String category, String name) {
        this.category = category;
        this.name = name;

    }


    protected KnownActivitiesRecord(Parcel in) {
        _id = in.readLong();
        category = in.readString();
        name = in.readString();
    }

    public static final Creator<KnownActivitiesRecord> CREATOR = new Creator<KnownActivitiesRecord>() {
        @Override
        public KnownActivitiesRecord createFromParcel(Parcel in) {
            return new KnownActivitiesRecord(in);
        }

        @Override
        public KnownActivitiesRecord[] newArray(int size) {
            return new KnownActivitiesRecord[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(category);
        dest.writeString(name);
    }
}
