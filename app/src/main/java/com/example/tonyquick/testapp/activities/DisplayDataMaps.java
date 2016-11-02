package com.example.tonyquick.testapp.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.tonyquick.testapp.utilities.MarkerCreator;
import com.example.tonyquick.testapp.R;
import com.example.tonyquick.testapp.utilities.ReviewDay;
import com.example.tonyquick.testapp.services.DatabaseHelper;
import com.example.tonyquick.testapp.models.ActivityRecord;
import com.example.tonyquick.testapp.models.GPSRecord;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayDataMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<GPSRecord> gpsData;
    private List<ActivityRecord> activities;
    private String date;
    private ArrayList<MarkerOptions> markers;
    private DatabaseHelper db;
    private PolylineOptions lines;
    private ArrayList<PolylineOptions> mLines;
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    CameraUpdate camUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data_maps);

        Bundle b = getIntent().getExtras();
        date = b.getString("date");
        db = DatabaseHelper.getInstance(this);
        gpsData = db.getGPSonDate(date);
        activities = db.getActivitiesByDate(date);
        double difference = 0;

        //for loop for GPS smoothing, very messy, tidy if you have time
        //for loop compare 3 values, if very quick u turn detected likely is an inaccurate reading
        //overwrite with average value of accurate readings

        for (int i = 0; i<gpsData.size()-6;i++){

            try {
                //parse string to date based on format
                Date d = format.parse(gpsData.get(i).getDate() + " " + gpsData.get(i).getTime());
                Date d2 = format.parse(gpsData.get(i+6).getDate() + " " + gpsData.get(i+6).getTime());

                difference = (d.getTime()-d2.getTime())/(1000*60);
                //check distance between times
            }catch (ParseException e) {
                Log.d("AJQ Log", "Exception");
            }

            if (ReviewDay.distance(
                    gpsData.get(i).getLatitude(),gpsData.get(i).getLongitude(),
                    gpsData.get(i+1).getLatitude(),gpsData.get(i+1).getLongitude())>30&& difference <2) {
                //if distance between 2 points is greater than 30 meters and if time difference is less than 2 minutes
                //repeat multiple times, this needs to be tidied :(

                if (ReviewDay.distance(gpsData.get(i).getLatitude(),gpsData.get(i).getLongitude(),gpsData.get(i+2).getLatitude(),gpsData.get(i+2).getLongitude())<15) {
                    gpsData.get(i+1).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+2).getLatitude())/2);
                    gpsData.get(i+1).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 2).getLongitude()) / 2);

                }
                else if (ReviewDay.distance(gpsData.get(i).getLatitude(),gpsData.get(i).getLongitude(),gpsData.get(i+3).getLatitude(),gpsData.get(i+3).getLongitude())<15) {
                    gpsData.get(i+1).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+3).getLatitude())/2);
                    gpsData.get(i+1).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 3).getLongitude()) / 2);

                    gpsData.get(i+2).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+3).getLatitude())/2);
                    gpsData.get(i+2).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 3).getLongitude()) / 2);

                }

                else if (ReviewDay.distance(gpsData.get(i).getLatitude(),gpsData.get(i).getLongitude(),gpsData.get(i+4).getLatitude(),gpsData.get(i+4).getLongitude())<15) {
                    gpsData.get(i+1).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+4).getLatitude())/2);
                    gpsData.get(i+1).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 4).getLongitude()) / 2);

                    gpsData.get(i+2).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+4).getLatitude())/2);
                    gpsData.get(i+2).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 4).getLongitude()) / 2);

                    gpsData.get(i+3).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+4).getLatitude())/2);
                    gpsData.get(i+3).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 4).getLongitude()) / 2);



                }
                else if (ReviewDay.distance(gpsData.get(i).getLatitude(),gpsData.get(i).getLongitude(),gpsData.get(i+5).getLatitude(),gpsData.get(i+5).getLongitude())<15) {
                    gpsData.get(i+1).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+5).getLatitude())/2);
                    gpsData.get(i+1).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 5).getLongitude()) / 2);

                    gpsData.get(i+2).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+5).getLatitude())/2);
                    gpsData.get(i+2).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 5).getLongitude()) / 2);

                    gpsData.get(i+3).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+5).getLatitude())/2);
                    gpsData.get(i+3).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 5).getLongitude()) / 2);

                    gpsData.get(i+4).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+5).getLatitude())/2);
                    gpsData.get(i+4).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 5).getLongitude()) / 2);



                }
                else if (ReviewDay.distance(gpsData.get(i).getLatitude(),gpsData.get(i).getLongitude(),gpsData.get(i+6).getLatitude(),gpsData.get(i+6).getLongitude())<15) {
                    gpsData.get(i+1).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+6).getLatitude())/2);
                    gpsData.get(i+1).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 6).getLongitude()) / 2);

                    gpsData.get(i+2).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+6).getLatitude())/2);
                    gpsData.get(i+2).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 6).getLongitude()) / 2);

                    gpsData.get(i+3).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+6).getLatitude())/2);
                    gpsData.get(i+3).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 6).getLongitude()) / 2);

                    gpsData.get(i+4).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+6).getLatitude())/2);
                    gpsData.get(i+4).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 6).getLongitude()) / 2);

                    gpsData.get(i+5).setLatitude((gpsData.get(i).getLatitude()+gpsData.get(i+6).getLatitude())/2);
                    gpsData.get(i+5).setLongitude((gpsData.get(i).getLongitude() + gpsData.get(i + 6).getLongitude()) / 2);



                }
            }


        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        plotGPSLines(); //call methods to plot overlays
        plotMarkers();
        LatLng pos = new LatLng(0,0);
        //set map location based on data displayed if available
        if (activities.size()>0){
            pos = MarkerCreator.posFirstMarker(activities.get(0));
        }else if(gpsData.size()>0) {
            pos = new LatLng(gpsData.get(0).getLatitude(),gpsData.get(0).getLongitude());
        }

        //update camera and set zoom
        camUp = CameraUpdateFactory.newLatLngZoom(pos,12);
        mMap.moveCamera(camUp);



    }

    public void plotGPSLines(){
        LatLng l;
        int counter = 0;
        mLines = new ArrayList<>();
        lines = new PolylineOptions();
        double difference = 0;

        //plot points to polyline
        //if time difference between 2 points greater than 2 minutes add current polyline to array list and create new polyline
        //lines will therefore not be displayed between points too far apart chronologically

        for (int i = 0;i<gpsData.size();i++) {
            if (i==0) {

                l = new LatLng(gpsData.get(i).getLatitude(), gpsData.get(i).getLongitude());
                lines.add(l);


            }else{

                l = new LatLng(gpsData.get(i).getLatitude(), gpsData.get(i).getLongitude());
                try {
                    Date d = format.parse(gpsData.get(i).getDate() + " " + gpsData.get(i).getTime());
                    Date d2 = format.parse(gpsData.get(i-1).getDate() + " " + gpsData.get(i-1).getTime());
                    difference = (d.getTime()-d2.getTime())/(1000*60);
                }catch (ParseException e) {
                    Log.d("AJQ Log", "Exception");
                }
                if(difference<2) {
                    lines.add(l);
                }else {
                    mLines.add(lines);
                    lines = new PolylineOptions();
                    lines.add(l);
                }


            }
        }
        mLines.add(lines);

        //add all polylines to map
        for (PolylineOptions lines : mLines){
            lines.width(5);
            mMap.addPolyline(lines);
        }



    }



    public void plotMarkers(){
        //create marker for each activity
        markers = new ArrayList<>();
        for(int i = 0;i<activities.size();i++){
            markers.add(MarkerCreator.createMarker(activities.get(i)));

        }
        //plot markers to map
        for (int i = 0;i<markers.size();i++){
            mMap.addMarker(markers.get(i));
        }


    }

}
