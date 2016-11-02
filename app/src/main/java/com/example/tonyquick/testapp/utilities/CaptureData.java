package com.example.tonyquick.testapp.utilities;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;


/**
 * Created by Tony Quick on 13/03/2016.
 */
public class CaptureData{

    Calendar cal;
    Location l1;
    ScheduledExecutorService ex;
    final long timeInterval = 20000;
    protected Context con;




    public CaptureData(Context c1){
        this.con = c1.getApplicationContext();




    }

    public void startCapture() {
        Log.d("AJQ Log","Starting data capture");



        //ScheduledExecutorService test = Executors.newScheduledThreadPool(5);
        //ex = Executors.newScheduledThreadPool(4);
        //ex.scheduleAtFixedRate(new GPSRunnable(), 0, 20, TimeUnit.SECONDS);


    }

    public void stopCapture(){
        ex.shutdown();
    }



}






