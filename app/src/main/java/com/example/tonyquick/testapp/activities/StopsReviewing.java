package com.example.tonyquick.testapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tonyquick.testapp.R;
import com.example.tonyquick.testapp.services.DatabaseHelper;
import com.example.tonyquick.testapp.models.PotentialStop;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class StopsReviewing extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<PotentialStop> potentialStops;
    private String date;
    TextView showDate, showStart, showFinish, showLat, showLon,titleLabel, alreadyExists;
    GoogleMap map;
    Button addActivity;
    Button ignore;
    LatLng latLng;
    CameraUpdate update;
    Boolean onStart = true;
    MapFragment myMap;
    DatabaseHelper db;

    private final int ACTIVITY_ADDED=10;

    private int counter=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_reviewing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Review Potential Stops");

        //define text boxes

        showDate = (TextView) findViewById(R.id.showDate);
        showStart = (TextView) findViewById(R.id.showStart);
        showFinish = (TextView) findViewById(R.id.showFinish);
        showLat = (TextView) findViewById(R.id.showLat);
        showLon = (TextView) findViewById(R.id.showLon);
        titleLabel = (TextView) findViewById(R.id.titleLabel);
        alreadyExists = (TextView) findViewById(R.id.alreadyExists);
        alreadyExists.setVisibility(View.INVISIBLE);

        addActivity = (Button) findViewById(R.id.newActivity);
        ignore = (Button) findViewById(R.id.ignore);

        Bundle b = getIntent().getExtras();
        potentialStops = b.getParcelableArrayList("stops");

        //get potential stops from extras in intent

        db = DatabaseHelper.getInstance(this);

        date = b.getString("Date");
        showDate.setText(date);

        //start map fragment and sync

        myMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        myMap.getMapAsync(this);

        updateInterface();


        //ignore button skips potential stop the next, update interface
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter < potentialStops.size() - 1) {
                    counter++;
                    updateInterface();
                }
                else{
                    finish();
                }

            }
        });


        //call android activity to add activity (wish they weren't both called activity)
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StopsReviewing.this,AddActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("potentialStop",potentialStops.get(counter));
                i.putExtras(b);
                startActivityForResult(i,ACTIVITY_ADDED);
            }
        });





    }



    private void updateInterface(){

        //user interface is updated based on details of potential stop

        showStart.setText(potentialStops.get(counter).getStartTime());
        showFinish.setText(potentialStops.get(counter).getStopTime());
        showLat.setText(Double.toString(potentialStops.get(counter).getLatitude()));
        showLon.setText(Double.toString(potentialStops.get(counter).getLongitude()));
        titleLabel.setText("Stop Detected   " + Integer.toString(counter+1)+"/"+Integer.toString(potentialStops.size()));

        //check if this activity already exists in database, if so, display message and disable button
        if (db.activityExists(date,potentialStops.get(counter).getStartTime(),potentialStops.get(counter).getStopTime())){
            addActivity.setEnabled(false);
            alreadyExists.setVisibility(View.VISIBLE);
        }else {
            //enable button if activity doesn't already exist
            addActivity.setEnabled(true);
            alreadyExists.setVisibility(View.INVISIBLE);
        }

        //dont do this for first potential stop, camera wont be ready and will crash
        if (onStart!=true) {

            //update camera poisition, clear old marker and add based on potential stop location
            latLng = new LatLng(potentialStops.get(counter).getLatitude(), potentialStops.get(counter).getLongitude());
            update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            map.animateCamera(update);
            map.clear();
            map.addMarker(new MarkerOptions().position(latLng).title("Stop Location"));


        }
        onStart=false;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //if an activity has been saved move to next potential stop and update interface
            case 10:
                if (resultCode==RESULT_OK){
                    if (counter<potentialStops.size()-1){
                        counter++;
                        updateInterface();
                    }else{
                        finish();
                    }
                }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //once map is ready save refernce to it
        //update camera to position of first stop

        this.map = googleMap;
        latLng = new LatLng(potentialStops.get(counter).getLatitude(),potentialStops.get(counter).getLongitude());
        update = CameraUpdateFactory.newLatLngZoom(latLng,15);


        map.moveCamera(update);
        map.addMarker(new MarkerOptions().position(latLng).title("Stop Location"));
    }
}
