package com.example.tonyquick.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tony Quick on 11/04/2016.
 */
//method to define knownlocation representing individual database record
//parcelable implmentation so it can be passed between activities

public class KnownLocationsRecord implements Parcelable{
    private long _id;
    private String name;
    private double longitude;
    private double latitude;

    public KnownLocationsRecord(long _id, String name, double longitude, double latitude) {
        this._id = _id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public KnownLocationsRecord(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected KnownLocationsRecord(Parcel in) {
        _id = in.readLong();
        name = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<KnownLocationsRecord> CREATOR = new Creator<KnownLocationsRecord>() {
        @Override
        public KnownLocationsRecord createFromParcel(Parcel in) {
            return new KnownLocationsRecord(in);
        }

        @Override
        public KnownLocationsRecord[] newArray(int size) {
            return new KnownLocationsRecord[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(name);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}
