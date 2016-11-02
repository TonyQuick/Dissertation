package com.example.tonyquick.testapp.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.tonyquick.testapp.utilities.GPSRunnable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Tony Quick on 10/03/2016.
 */
public class LocationClient_Singleton implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static LocationClient_Singleton instance;
    private GoogleApiClient client;
    private Context con;

    PowerManager pm;
    PowerManager.WakeLock wl;

    //private constructor, singleton instance obtainable through getinstance

    private LocationClient_Singleton(Context con) {
        client = new GoogleApiClient.Builder(con)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //client.connect();
        this.con = con;


    }

    //singleton contructor to fetch instance

    public static synchronized LocationClient_Singleton getInstance(Context con) {
        if (instance == null) {

            instance = new LocationClient_Singleton(con);


        }
        return instance;

    }

    public Location getLastLocation() {
        Location l = null;
    //get last known location if permission allows


        if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            l = LocationServices.FusedLocationApi.getLastLocation(client);
        }

        return l;
    }


    @Override
    public void onConnected(Bundle bundle) {

            //once connected request location updates
        //obtain wakelock

        //// TODO: 25/04/2016 Wakelock won't work from here, implement as service

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(9500);
        request.setFastestInterval(9500);
        if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);

        }

        pm = (PowerManager) con.getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");
        wl.acquire(86400000);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("AJQ log", "Failed");

    }

    public void connect(){
        client.connect();
    }

    public void disconnect(){

        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        client.disconnect();
        wl.release();

    }


    @Override
    public void onLocationChanged(Location location) {

        //call gps runnable with lat and lon vals
        double lon = location.getLongitude();
        double lat = location.getLatitude();
        new GPSRunnable(lon,lat,con).run();

    }


}
