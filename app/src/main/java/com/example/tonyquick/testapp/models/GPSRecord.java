package com.example.tonyquick.testapp.models;

/**
 * Created by Tony Quick on 14/03/2016.
 */

//method to define activity representing individual database record
public class GPSRecord {

    private long _id;
    private double longitude;
    private double latitude;
    private String date;
    private String time;

    //constructor without id
    public GPSRecord(double longitude, double latitude, String date, String time){
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.time = time;
    }

    //constructor with id
    public GPSRecord(long _id, double longitude, double latitude, String date, String time) {
        this._id = _id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.time = time;
    }

    public long get_id() {
        return _id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Time: " + time + " Lat: " + latitude + " Lon: " + longitude;
    }
}
