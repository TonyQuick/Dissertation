package com.example.tonyquick.testapp.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tonyquick.testapp.models.ActivityRecord;
import com.example.tonyquick.testapp.models.GPSRecord;
import com.example.tonyquick.testapp.models.KnownActivitiesRecord;
import com.example.tonyquick.testapp.models.KnownLocationsRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony Quick on 13/03/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    //final definitions for all database variables

    private static final int DATABASE_VERSION =3;
    private static final String DATABASE_NAME = "appdata.db";

    public static final String GPS_TABLE = "gpsdata";
    public static final String GPS_ID = "_id";
    public static final String GPS_LON = "gpslongitude";
    public static final String GPS_LAT = "gpslatitude";
    public static final String GPS_DATE = "datestamp";
    public static final String GPS_TIME = "timestamp";

    public static final String KNOWN_LOCATIONS_TABLE = "knownlocations";
    public static final String KNOWN_LOCATIONS_ID = "_id";
    public static final String KNOWN_LOCATIONS_NAME = "name";
    public static final String KNOWN_LOCATIONS_LON = "loclongitude";
    public static final String KNOWN_LOCATIONS_LAT = "loclatitude";

    public static final String KNOWN_ACTIVITIES_TABLE = "knownactivities";
    public static final String KNOWN_ACTIVITIES_ID = "_id";
    public static final String KNOWN_ACTIVITIES_CATEGORY = "category";
    public static final String KNOWN_ACTIVITIES_NAME = "name";

    public static final String ACTIVITY_TABLE = "activity";
    public static final String ACTIVITY_ID = "_id";
    public static final String ACTIVITY_LOCATION = "locid";
    public static final String ACTIVITY_ACTIVITY = "actid";
    public static final String ACTIVITY_DATE = "actdate";
    public static final String ACTIVITY_START_TIME = "starttime";
    public static final String ACTIVITY_FINISH_TIME = "finishtime";



    public static final String createGPSTable = "CREATE TABLE " + GPS_TABLE + "("+
            GPS_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
            GPS_LON + " DOUBLE," +
            GPS_LAT + " DOUBLE," +
            GPS_DATE + " DATE," +
            GPS_TIME + " TIME);";

    public static final String createKnownLocationTable = "CREATE TABLE " + KNOWN_LOCATIONS_TABLE + "("+
            KNOWN_LOCATIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KNOWN_LOCATIONS_NAME + " TEXT," +
            KNOWN_LOCATIONS_LON + " DOUBLE," +
            KNOWN_LOCATIONS_LAT + " DOUBLE);";

    public static final String createKnownActivitiesTable = "CREATE TABLE " + KNOWN_ACTIVITIES_TABLE + "("+
            KNOWN_ACTIVITIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KNOWN_ACTIVITIES_CATEGORY + " TEXT," +
            KNOWN_ACTIVITIES_NAME + " TEXT);";


    public static final String createActivityTable = "CREATE TABLE "+ ACTIVITY_TABLE + "(" +
            ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ACTIVITY_LOCATION + " INTEGER," +
            ACTIVITY_ACTIVITY + " INTEGER," +
            ACTIVITY_DATE + " DATE," +
            ACTIVITY_START_TIME + " TIME," +
            ACTIVITY_FINISH_TIME + " TIME);";






    //private instantion method, can only be called via get instance
    private DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    public static synchronized DatabaseHelper getInstance(Context context) {
    //singleton method, return instance if exists, else create using app context

        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }





    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createGPSTable);
        db.execSQL(createKnownLocationTable);
        db.execSQL(createKnownActivitiesTable);
        db.execSQL(createActivityTable);
        updateKnownactivities();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+GPS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+KNOWN_LOCATIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+KNOWN_ACTIVITIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ACTIVITY_TABLE);
        onCreate(db);
    }

    public void updateKnownactivities (){
        //create known activities records
        SQLiteDatabase db = getWritableDatabase();
        addKnownActivity(new KnownActivitiesRecord("Business", "Work"));
        addKnownActivity(new KnownActivitiesRecord("Business","Meeting"));
        addKnownActivity(new KnownActivitiesRecord("Business","Appointment"));
        addKnownActivity(new KnownActivitiesRecord("Leisure","Sport"));
        addKnownActivity(new KnownActivitiesRecord("Leisure","Eating"));
        addKnownActivity(new KnownActivitiesRecord("Leisure","Relaxing"));
        addKnownActivity(new KnownActivitiesRecord("Leisure","Arts and Entertainment"));
        addKnownActivity(new KnownActivitiesRecord("Leisure", "Shopping"));
        addKnownActivity(new KnownActivitiesRecord("Tourism", "Eating"));
        addKnownActivity(new KnownActivitiesRecord("Tourism", "Visiting Attraction"));
        addKnownActivity(new KnownActivitiesRecord("Tourism", "Shopping"));
        db.close();



    }

    /*
    Definitions for all queries required by other classes
    */


    public void addGPS(GPSRecord gps){

        ContentValues values = new ContentValues();
        values.put(GPS_LON, gps.getLongitude());
        values.put(GPS_LAT, gps.getLatitude());
        values.put(GPS_DATE, gps.getDate());
        values.put(GPS_TIME, gps.getTime());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(GPS_TABLE, null, values);
        db.close();

    }

    public void addActivity(ActivityRecord aR){
        ContentValues values = new ContentValues();
        values.put(ACTIVITY_LOCATION,aR.getLocationid());
        values.put(ACTIVITY_ACTIVITY,aR.getActivityid());
        values.put(ACTIVITY_DATE,aR.getDate());
        values.put(ACTIVITY_START_TIME,aR.getStartTime());
        values.put(ACTIVITY_FINISH_TIME, aR.getFinishTime());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(ACTIVITY_TABLE, null, values);
        db.close();


    }

    public KnownLocationsRecord addKnownLocation(KnownLocationsRecord klr){

        //add location to database based on KnownLocationRecord klr contents, updates object with primary key and returns

        long index;
        ContentValues values = new ContentValues();
        values.put(KNOWN_LOCATIONS_NAME,klr.getName());
        values.put(KNOWN_LOCATIONS_LON, klr.getLongitude());
        values.put(KNOWN_LOCATIONS_LAT, klr.getLatitude());
        SQLiteDatabase db = getWritableDatabase();
        index = db.insert(KNOWN_LOCATIONS_TABLE,null,values);
        db.close();
        klr.set_id(index);


        return klr;

    }

    public KnownActivitiesRecord addKnownActivity (KnownActivitiesRecord kar){

        //add activity to database based on KnownAcitivyRecord kar contents, updates object with primary key and returns

        long index;
        ContentValues values = new ContentValues();
        values.put(KNOWN_ACTIVITIES_CATEGORY, kar.getCategory());
        values.put(KNOWN_ACTIVITIES_NAME, kar.getName());
        SQLiteDatabase db = getWritableDatabase();
        index = db.insert(KNOWN_ACTIVITIES_TABLE,null,values);
        db.close();
        kar.set_id(index);



        return kar;
    }






    public List<GPSRecord> getGPSonDate(String dat){
        long _id;
        double latitude;
        double longitude;
        String date;
        String time;

        List<GPSRecord> gpsData = new ArrayList<>();
        String query = "Select * from " + GPS_TABLE + " WHERE " + GPS_DATE + " = " + "date('"+dat+"')";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            _id = c.getLong(c.getColumnIndex(GPS_ID));
            latitude = c.getDouble(c.getColumnIndex(GPS_LAT));
            longitude = c.getDouble(c.getColumnIndex(GPS_LON));
            date = c.getString(c.getColumnIndex(GPS_DATE));
            time = c.getString(c.getColumnIndex(GPS_TIME));
            GPSRecord gps = new GPSRecord(_id,longitude,latitude,date,time);
            gpsData.add(gps);
        }
        db.close();


        return gpsData;
    }

    public List<KnownLocationsRecord> getKnownLocations(){
        long _id;
        String name;
        double longitude;
        double latitude;

        List<KnownLocationsRecord> knownLocations = new ArrayList<>();
        String query = "Select * from " + KNOWN_LOCATIONS_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query,null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            _id = c.getLong(c.getColumnIndex(KNOWN_LOCATIONS_ID));
            name = c.getString(c.getColumnIndex(KNOWN_LOCATIONS_NAME));
            longitude = c.getDouble(c.getColumnIndex(KNOWN_LOCATIONS_LON));
            latitude = c.getDouble(c.getColumnIndex(KNOWN_LOCATIONS_LAT));
            KnownLocationsRecord klr = new KnownLocationsRecord(_id,name,longitude,latitude);
            knownLocations.add(klr);
        }
        db.close();


        return knownLocations;
    }

    public List<KnownActivitiesRecord> getKnownActivities() {
        long _id;
        String category;
        String name;

        List<KnownActivitiesRecord> knownActivities = new ArrayList<>();
        String query = "Select * from " + KNOWN_ACTIVITIES_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query,null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            _id = c.getLong(c.getColumnIndex(KNOWN_ACTIVITIES_ID));
            category = c.getString(c.getColumnIndex(KNOWN_ACTIVITIES_CATEGORY));
            name = c.getString(c.getColumnIndex(KNOWN_ACTIVITIES_NAME));
            KnownActivitiesRecord kar = new KnownActivitiesRecord(_id,category,name);
            knownActivities.add(kar);
        }
        db.close();



        return knownActivities;
    }

    public KnownActivitiesRecord getActivityByID(long id){

        KnownActivitiesRecord kar;
        String query = "Select * from " + KNOWN_ACTIVITIES_TABLE + " WHERE " + KNOWN_ACTIVITIES_ID + " = " + "("+id+")";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        String category = c.getString(c.getColumnIndex(KNOWN_ACTIVITIES_CATEGORY));
        String name = c.getString(c.getColumnIndex(KNOWN_ACTIVITIES_NAME));
        kar = new KnownActivitiesRecord(id,category,name);
        db.close();

        return kar;
    }

    public KnownLocationsRecord getLocationByID(long id){
        //get location by id
        KnownLocationsRecord klr;
        String query = "Select * from " + KNOWN_LOCATIONS_TABLE + " WHERE " + KNOWN_LOCATIONS_ID + " = " +id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query,null);

        c.moveToFirst();
        String name = c.getString(c.getColumnIndex(KNOWN_LOCATIONS_NAME));
        double lon = c.getDouble(c.getColumnIndex(KNOWN_LOCATIONS_LON));
        double lat = c.getDouble(c.getColumnIndex(KNOWN_LOCATIONS_LAT));
        klr = new KnownLocationsRecord(id,name,lon,lat);
        db.close();

        return klr;
    }




    public List<ActivityRecord> getActivitiesByDate(String dat){
        //return list of activities based on date
        long _id;
        long locationid;
        long activityid;
        String date;
        String startTime;
        String finishTime;

        List<ActivityRecord> activities = new ArrayList<>();
        String query = "Select * from " + ACTIVITY_TABLE + " WHERE " + ACTIVITY_DATE + " = " + "date('"+dat+"')";

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            _id = c.getLong(c.getColumnIndex(ACTIVITY_ID));
            locationid = c.getLong(c.getColumnIndex(ACTIVITY_LOCATION));
            activityid = c.getLong(c.getColumnIndex(ACTIVITY_ACTIVITY));
            date = c.getString(c.getColumnIndex(ACTIVITY_DATE));
            startTime = c.getString(c.getColumnIndex(ACTIVITY_START_TIME));
            finishTime = c.getString(c.getColumnIndex(ACTIVITY_FINISH_TIME));
            ActivityRecord ar = new ActivityRecord(_id,locationid,activityid,date,startTime,finishTime);



            //activities.add(ar);
        }
        db.close();

        return activities;
    }

    public List<ActivityRecord> getActivities(){
        long _id;
        long locationid;
        long activityid;
        String date;
        String startTime;
        String finishTime;

        List<ActivityRecord> activities = new ArrayList<>();
        String query = "Select * from " + ACTIVITY_TABLE;

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            _id = c.getLong(c.getColumnIndex(ACTIVITY_ID));
            locationid = c.getLong(c.getColumnIndex(ACTIVITY_LOCATION));
            activityid = c.getLong(c.getColumnIndex(ACTIVITY_ACTIVITY));
            date = c.getString(c.getColumnIndex(ACTIVITY_DATE));
            startTime = c.getString(c.getColumnIndex(ACTIVITY_START_TIME));
            finishTime = c.getString(c.getColumnIndex(ACTIVITY_FINISH_TIME));
            ActivityRecord ar = new ActivityRecord(_id,locationid,activityid,date,startTime,finishTime);

            activities.add(ar);
        }
        db.close();

        return activities;
    }

    public boolean activityExists(String date, String startTime, String finishTime){
        //check if activity already exists in table
        Boolean exists = false;
        String query = "Select * from " + ACTIVITY_TABLE + " WHERE " + ACTIVITY_DATE + " = " + "date('"+date+"')"+
                " AND " + ACTIVITY_START_TIME + " = time('" + startTime + "')" +
                " AND " + ACTIVITY_FINISH_TIME + " = time('" + finishTime + "')";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount()>0)
            exists = true;

        db.close();

        return exists;
    }

    public List<KnownActivitiesRecord> getKnownActivitiesByCategory(String cat){
        long _id;
        String category;
        String name;

        List<KnownActivitiesRecord> knownActivities = new ArrayList<>();
        String query = "Select * from " + KNOWN_ACTIVITIES_TABLE + " WHERE "+ KNOWN_ACTIVITIES_CATEGORY+ " = " + "'"+cat+"'";
        SQLiteDatabase db = getReadableDatabase();
        Log.d("AJQ Log", query);
        Cursor c = db.rawQuery(query, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            _id = c.getLong(c.getColumnIndex(KNOWN_ACTIVITIES_ID));
            category = c.getString(c.getColumnIndex(KNOWN_ACTIVITIES_CATEGORY));
            name = c.getString(c.getColumnIndex(KNOWN_ACTIVITIES_NAME));
            KnownActivitiesRecord kar = new KnownActivitiesRecord(_id,category,name);
            knownActivities.add(kar);
        }
        db.close();



        return knownActivities;
    }



}
