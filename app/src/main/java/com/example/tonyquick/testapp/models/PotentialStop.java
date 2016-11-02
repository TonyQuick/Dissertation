package com.example.tonyquick.testapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tony Quick on 29/03/2016.
    potential stops are objects created by review day class, represents a single stop
    parcelable implementation required to pass between activities
 */


public class PotentialStop implements Parcelable {
    private String date;
    private String startTime;
    private String stopTime;
    private double longitude;
    private double latitude;

    //default number based on number of records already applied to rolling average
    private int counter = 30;

    public PotentialStop(String date, String startTime, double longitude, double latitude) {
        this.date = date;
        this.startTime = startTime;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    protected PotentialStop(Parcel in) {
        date = in.readString();
        startTime = in.readString();
        stopTime = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        counter = in.readInt();
    }

    public static final Creator<PotentialStop> CREATOR = new Creator<PotentialStop>() {
        @Override
        public PotentialStop createFromParcel(Parcel in) {
            return new PotentialStop(in);
        }

        @Override
        public PotentialStop[] newArray(int size) {
            return new PotentialStop[size];
        }
    };

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public void rollingAverage(Double lon, Double lat){
        this.longitude += (lon-this.longitude) /++counter;  //rolling average for stop location based on new points within stop
        this.latitude += (lat-this.latitude) /counter;

    }

    @Override
    public String toString() {
        return "Longitude :" + this.longitude + " Latitude: " + this.latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(stopTime);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeInt(counter);
    }

}
