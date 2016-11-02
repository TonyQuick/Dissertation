package com.example.tonyquick.testapp.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.tonyquick.testapp.services.GPSSampler;
import com.example.tonyquick.testapp.models.PotentialStop;
import com.example.tonyquick.testapp.R;
import com.example.tonyquick.testapp.utilities.ReviewDay;
import com.example.tonyquick.testapp.utilities.CaptureData;
import com.example.tonyquick.testapp.services.DatabaseHelper;
import com.example.tonyquick.testapp.services.LocationClient_Singleton;
import com.example.tonyquick.testapp.models.GPSRecord;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Boolean capturingData = false;
    CaptureData cData;
    DatabaseHelper db;
    PowerManager pm;
    PowerManager.WakeLock wl;
    LocationClient_Singleton client;
    private static Context context;
    private Boolean permGranted;
    private Button addActivity;
    private Button captureData;



    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 2;

    private static final int GET_DATE_FOR_REVIEW_DATA = 0;
    private static final int GET_DATE_FOR_DISPLAY_DATA = 1;

    private static Boolean finePermission = false;
    private static Boolean coarsePermission = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkPermissions();

        //Menu m = (Menu) findViewById(R.id.action_settings);

        MainActivity.context = getApplicationContext();
        cData = new CaptureData(this.getApplicationContext());

        //create database helper instance
        db = DatabaseHelper.getInstance(this.getApplicationContext());
        client = LocationClient_Singleton.getInstance(this.getApplicationContext());


        //define buttons
        addActivity = (Button) findViewById(R.id.newActivity);
        captureData = (Button) findViewById(R.id.dataCollection);


        final Button reviewData = (Button) findViewById(R.id.reviewData);
        final Button dispData = (Button) findViewById(R.id.dispData);


        Toast.makeText(this,"Let's go",Toast.LENGTH_SHORT).show();

        addActivity.setEnabled(false);


        if (isServiceRunning(GPSSampler.class)){
            capturingData = true;
            captureData.setText("Disable GPS Collection");

        }


        captureData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capturingData == false) {

                    //start service for GPS capture
                    Intent i = new Intent(getBaseContext(),GPSSampler.class);
                    startService(i);

                    isServiceRunning(GPSSampler.class);


                    Snackbar mySB = Snackbar.make(findViewById(R.id.snackbarTest), "GPS capturing active", Snackbar.LENGTH_SHORT);
                    mySB.show();
                    capturingData = true;
                    captureData.setText("Disable GPS collection");
                    addActivity.setEnabled(true);

                } else if (capturingData == true) {


                    //are you sure? message to user, handled later
                    AlertDialog.Builder cancel = new AlertDialog.Builder(MainActivity.this);
                    cancel.setMessage("Are you sure you wish to cancel GPS collection?");
                    cancel.setPositiveButton("Yes", cancelGPS);
                    cancel.setNegativeButton("No", cancelGPS);


                    cancel.create();
                    cancel.show();



                }


            }
        });

        dispData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(MainActivity.this, DateSelector.class);    //create intent for get date
                startActivityForResult(int1, GET_DATE_FOR_DISPLAY_DATA);        //get date
            }
        });



        reviewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,DateSelector.class);
                startActivityForResult(i,GET_DATE_FOR_REVIEW_DATA);

            }
        });


    }


    private void reviewDayByDate(String date){
        List<GPSRecord> gpsData;
        ArrayList<PotentialStop> pStops = new ArrayList<>();

        //get data from db
        gpsData = db.getGPSonDate(date);

        //check data isnt empty
        if (gpsData.size()==0){
            Toast.makeText(this,"No GPS data found for this data",Toast.LENGTH_LONG).show();
        }
        else {
            pStops = ReviewDay.calculatePotentialStops(gpsData);



            if (pStops.size() == 0) {
                //check there is atleast 1 stop
                Toast.makeText(this, "No potential stops found on this day", Toast.LENGTH_LONG).show();
            } else {
                Intent i = new Intent(MainActivity.this, StopsReviewing.class);
                Bundle b = new Bundle();

                //call method to review stops, pass pStops as arguement
                b.putParcelableArrayList("stops", pStops);

                b.putString("Date", date);
                i.putExtras(b);
                startActivity(i);


            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                //switch case to determine which activity has returned a result
                //activity review data
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String date = b.getString("date");
                    reviewDayByDate(date);      //call method to review data from date selected
                    break;
                }
            case 1:

                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String date = b.getString("date");

                    Intent i = new Intent(MainActivity.getAppContext(),DisplayDataMaps.class);
                    Bundle bun = new Bundle();
                    bun.putString("date",date);
                    i.putExtras(bun);

                    startActivity(i);       //call map activity to display data


                }
        }
    }

    public static Context getAppContext(){
        return MainActivity.context;
    }

    private void checkPermissions(){

        //check user permissions have been granted, if not request them
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            //return results for user permissions
            case MY_PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    finePermission = true;
                    Log.d("AJQ Log","Fine location granted");
                }
                else{
                    Log.d("AJQ Log","Fine location refused");
                }

            case MY_PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    coarsePermission = true;

                }

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isServiceRunning(Class<?> service){
        //check if GPS capture service is running
        ActivityManager m = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo s: m.getRunningServices(Integer.MAX_VALUE)){

            if (service.getName().equals(s.service.getClassName())){

                return true;
            }
        }

        return false;



    }

    DialogInterface.OnClickListener cancelGPS = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    //if user selects yes, end GPS capture service
                    Snackbar mySB = Snackbar.make(findViewById(R.id.snackbarTest), "GPS capturing deactivated", Snackbar.LENGTH_SHORT);
                    mySB.show();
                    capturingData = false;
                    captureData.setText("Activate GPS collection");
                    addActivity.setEnabled(false);
                    Intent i = new Intent(getBaseContext(),GPSSampler.class);
                    stopService(i);


                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //else do nothing
                    break;
            }

        }

    };

}
