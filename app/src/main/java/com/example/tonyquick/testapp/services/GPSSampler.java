package com.example.tonyquick.testapp.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.example.tonyquick.testapp.utilities.GPSRunnable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Tony Quick on 27/03/2016.
 */
public class GPSSampler extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    PowerManager pm;
    PowerManager.WakeLock wl;
    private GoogleApiClient client;
    private Context con;


    //blank constructor
    public GPSSampler(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        //context obtained from main app context
        con = this.getApplicationContext();
        super.onCreate();
        client = new GoogleApiClient.Builder(this.getApplicationContext())      //create new location client
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client.connect();                                                       //attempt connection
        pm = (PowerManager) this.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");         //get wake lock for full day
        wl.acquire(86400000);


    }
    //listen for location updates, call gps runnable passing lat and lon values
    private LocationListener listen = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double lon = location.getLongitude();
            double lat = location.getLatitude();
            new GPSRunnable(lon,lat,con).run();

        }
    };

    @Override
    public void onDestroy() {
        //release wakelock, cancel locaation updates disconnect from location client on death
        wl.release();
        LocationServices.FusedLocationApi.removeLocationUpdates(client, listen);
        client.disconnect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //once connection established request updates from gps sensor
        //high accuracy setting applied
        //check application for permissions


        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(9500);
        request.setFastestInterval(9500);
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, request, listen);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


    }



}
