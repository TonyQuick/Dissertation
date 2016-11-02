package com.example.tonyquick.testapp.utilities;

import android.content.Context;
import android.location.Location;

import com.example.tonyquick.testapp.services.DatabaseHelper;
import com.example.tonyquick.testapp.services.LocationClient_Singleton;
import com.example.tonyquick.testapp.models.GPSRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tony Quick on 16/03/2016.
 */
public class GPSRunnable implements Runnable {

    private Calendar c;
    private Date d;

    private Location l;
    private LocationClient_Singleton client;
    private double lat;
    private double lon;


    protected Context con;
    private String dat;
    private String tim;



    public GPSRunnable(double lon, double lat, Context con){
        this.lon = lon;
        this.lat = lat;
        this.con = con;
    }

    @Override
    public void run() {

       //get current system date and time and convert to formats understood by sqlite

        c = Calendar.getInstance();
        d = c.getTime();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
        tim = time.format(d);
        dat = date.format(d);

        //get db helper using context from service
        //add new gps record to db

        GPSRecord gps = new GPSRecord(lon,lat,dat,tim);
        DatabaseHelper helper = DatabaseHelper.getInstance(con);
        helper.addGPS(gps);

    }


}
