package com.example.tonyquick.testapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tonyquick.testapp.models.PotentialStop;
import com.example.tonyquick.testapp.R;
import com.example.tonyquick.testapp.utilities.ReviewDay;
import com.example.tonyquick.testapp.services.DatabaseHelper;
import com.example.tonyquick.testapp.models.ActivityRecord;
import com.example.tonyquick.testapp.models.KnownActivitiesRecord;
import com.example.tonyquick.testapp.models.KnownLocationsRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner locationSpin, activitySpin, categorySpin;   //drop down menus
    PotentialStop p;
    List<KnownActivitiesRecord> knownActivities;
    List<KnownLocationsRecord> knownLocations, closeLocations;
    KnownActivitiesRecord karReturned, karSelected;
    KnownLocationsRecord klrReturned, klrSelected;
    DatabaseHelper db;
    Double distance;
    ArrayAdapter actSpinAdapter, locSpinAdapter, categoryAdapter, testSpinAdapter;



    Button cancel, confirm, addKnownLoc, addKnownAct;
    private final int NEW_LOCATION_CODE = 20;
    private final int NEW_ACTIVITY_CODE = 21;
    List<String> categories = Arrays.asList("Business", "Leisure", "Tourism");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = DatabaseHelper.getInstance(this);          //get instance of db helper
        knownLocations = db.getKnownLocations();
        Bundle b = getIntent().getExtras();
        p = b.getParcelable("potentialStop");
        closeLocations = new ArrayList<KnownLocationsRecord>();


        getCloseLocations();

        categorySpin = (Spinner) findViewById(R.id.categorySpin);
        categoryAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,categories);
        categorySpin.setAdapter(categoryAdapter);
        categorySpin.setOnItemSelectedListener(this);

        knownActivities = db.getKnownActivitiesByCategory((String)categorySpin.getSelectedItem());
        //get activities based on category spinner

        locationSpin = (Spinner) findViewById(R.id.locationSpin);
        activitySpin = (Spinner) findViewById(R.id.activitySpin);
        locSpinAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,closeLocations);
        actSpinAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,knownActivities);
        //testSpinAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,);
        //define spinner adapters


        //if either spinner adapter is empty, do not apply, will crash
        if (closeLocations.size()>0) {
            locationSpin.setAdapter(locSpinAdapter);
        }else{
            locationSpin.setAdapter(null);
        }

        if (knownActivities.size()>0) {
            activitySpin.setAdapter(actSpinAdapter);
        }else{
            activitySpin.setAdapter(null);
        }

        cancel = (Button) findViewById(R.id.cancel);
        confirm = (Button) findViewById(R.id.confirm);
        addKnownLoc = (Button) findViewById(R.id.newLocation);
        addKnownAct = (Button) findViewById(R.id.newActivity);



        //return activity cancelled, no activity added
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(RESULT_CANCELED,i);
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check both actvitiy and location selected
                //if true add new activity to database and return success
                if(activitySpin.getSelectedItem()==null){
                    Toast.makeText(getApplicationContext(),"No Activity Selected",Toast.LENGTH_LONG).show();
                    return;
                }
                if(locationSpin.getSelectedItem()==null){
                    Toast.makeText(getApplicationContext(),"No Location Selected",Toast.LENGTH_LONG).show();
                    return;
                }

                karSelected = (KnownActivitiesRecord) activitySpin.getSelectedItem();
                klrSelected = (KnownLocationsRecord) locationSpin.getSelectedItem();


                ActivityRecord aR = new ActivityRecord(klrSelected.get_id(),karSelected.get_id(),p.getDate(),p.getStartTime(),p.getStopTime());

                db.addActivity(aR);

                Intent i = new Intent();
                setResult(RESULT_OK,i);
                finish();

            }
        });

        addKnownLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddActivity.this, NewLocation.class);
                Bundle b = new Bundle();
                b.putParcelable("potentialStop",p);
                i.putExtras(b);
                startActivityForResult(i, NEW_LOCATION_CODE);
            //button to add a new location, call android activity
            }
        });

        addKnownAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddActivity.this,NewActivity.class);
                startActivityForResult(i,NEW_ACTIVITY_CODE);
                //button to add a new activity, call android activity
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle b = data.getExtras();
        switch (requestCode){
            case NEW_ACTIVITY_CODE:


                //add new activity created to spinner and select

                if (resultCode==RESULT_OK) {
                    karReturned = b.getParcelable("knownActivity");
                    knownActivities.add(karReturned);
                    actSpinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, knownActivities);
                    activitySpin.setAdapter(actSpinAdapter);
                    activitySpin.setSelection(knownActivities.size() - 1);
                    addKnownAct.setEnabled(false);
                }
                break;

            case NEW_LOCATION_CODE:

                //add new location to spinner and select
                if(resultCode==RESULT_OK) {
                    klrReturned = b.getParcelable("knownLocation");
                    closeLocations.add(klrReturned);
                    locSpinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, closeLocations);
                    locationSpin.setAdapter(locSpinAdapter);
                    locationSpin.setSelection(closeLocations.size() - 1);
                    addKnownLoc.setEnabled(false);
                }
                break;

        }

    }

    private void getCloseLocations(){
        //method to determine which of the known locations are within range of the stop

        for (int i =0; i<knownLocations.size();i++){

            Log.d("AJQ Log", "Known Locations Lon: "+ Double.toString(knownLocations.get(i).getLongitude())+ " Lat: "+Double.toString(knownLocations.get(i).getLatitude()));
            distance = ReviewDay.distance(p.getLatitude(), p.getLongitude(), knownLocations.get(i).getLatitude(), knownLocations.get(i).getLongitude());
            if (distance < 150){
                closeLocations.add(knownLocations.get(i));
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //update activities based on category selected from category spinner

        if(parent.getId()==R.id.categorySpin){
            String category = (String)categorySpin.getSelectedItem();
            knownActivities = db.getKnownActivitiesByCategory(category);
            actSpinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, knownActivities);
            activitySpin.setAdapter(actSpinAdapter);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
