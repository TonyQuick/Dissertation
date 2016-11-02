package com.example.tonyquick.testapp.utilities;

import com.example.tonyquick.testapp.services.DatabaseHelper;
import com.example.tonyquick.testapp.activities.MainActivity;
import com.example.tonyquick.testapp.models.ActivityRecord;
import com.example.tonyquick.testapp.models.KnownActivitiesRecord;
import com.example.tonyquick.testapp.models.KnownLocationsRecord;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Tony Quick on 27/04/2016.
 */
public class MarkerCreator {


//class to create custom markers for map overlays

    public static MarkerOptions createMarker(ActivityRecord actRec){


        MarkerOptions options = new MarkerOptions();
        DatabaseHelper db= DatabaseHelper.getInstance(MainActivity.getAppContext());
        KnownLocationsRecord klr = db.getLocationByID(actRec.getLocationid());  //get loc by foreign key id
        KnownActivitiesRecord kar = db.getActivityByID(actRec.getActivityid()); //same for known act

        //define marker snippet and location based on activity details

        LatLng pos = new LatLng(klr.getLatitude(),klr.getLongitude());
        String title = klr.getName();
        String snippet = "Activity: " + kar.getName() + ". Start: " + actRec.getStartTime() + " Finish: "+actRec.getFinishTime();
        String category = kar.getCategory();


        options.position(pos).title(title).snippet(snippet);

        //swtich for marker colour based on category of marker
        switch (category){
            case "Business":
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                break;

            case "Leisure":
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                break;

            case "Tourism":
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                break;

        }




        return options;
    }

    //method to obtain position of first marker for map positioning

    public static LatLng posFirstMarker (ActivityRecord act){
        LatLng l;
        DatabaseHelper db= DatabaseHelper.getInstance(MainActivity.getAppContext());
        KnownLocationsRecord klr = db.getLocationByID(act.getLocationid());
        l = new LatLng(klr.getLatitude(),klr.getLongitude());

        return l;
    }

}
