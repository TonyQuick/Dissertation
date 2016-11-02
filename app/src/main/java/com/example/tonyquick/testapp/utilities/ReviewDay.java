package com.example.tonyquick.testapp.utilities;

import com.example.tonyquick.testapp.models.GPSRecord;
import com.example.tonyquick.testapp.models.PotentialStop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tony Quick on 29/03/2016.
 */
public class ReviewDay {

    //method housing algorithms that detect when a user has stopped
    //returns an arraylist of potential stop objects


    public static ArrayList<PotentialStop> calculatePotentialStops(List<GPSRecord> gpsData){

        int timeRequiredForStop = 30;        //no minutes within area to be considered a stop
        int timeStopEnded = 15;              //time out of area for stop to be considered over


        ArrayList<PotentialStop> potStops = new ArrayList<>();
        List<GPSRecord> tempList;
        List<GPSRecord> tempListFin;
        boolean isStopped;

        //for each gps record
        for(int i = 0; i<gpsData.size()-timeRequiredForStop;i++){
            tempList = gpsData.subList(i, i + timeRequiredForStop);     //create sublist for all reading to be compared
            isStopped = detectStop(tempList);                           //check if stop occurs

            if (isStopped == true){                                     //enter if statement if stop is detected

                double aveLon = averageOfLon(tempList);                 //get average location of all points within stop location so far
                double aveLat = averageOfLat(tempList);

                PotentialStop p = new PotentialStop(                //create new potential stop object
                        gpsData.get(i).getDate(),
                        gpsData.get(i).getTime(),
                        aveLon,
                        aveLat);
                i=i+timeRequiredForStop-1;                          //increment i to skip all points all determined to be within stop
                while (isStopped == true&& i<gpsData.size()){       //while loop to continue to check for end of stop


                    tempListFin =gpsData.subList(i,i+timeStopEnded>gpsData.size()?gpsData.size():i+timeStopEnded);      //new sublist for all readings needed to detect end of stop
                    isStopped = detectLeave(p,tempListFin);                                                             //check if stop ends
                    if (isStopped ==true){                                                                              //if still stopped apply current reading to rolling average and increment i
                        p.rollingAverage(gpsData.get(i).getLongitude(),gpsData.get(i).getLatitude());
                        i++;
                    }
                }
                p.setStopTime(gpsData.get(i-1).getTime());      //will exit is stop ends, set end time based on last reading still within stop range
                potStops.add(p);                                //add potential stop to arraylist
            }
        }
        return potStops;
    }

    private static boolean detectStop (List<GPSRecord> gpsData){
        boolean stopDetected = false;
        double distance;
        double percentageInRange;
        int numInRange=0;
        int totalNo = gpsData.size();

                                                                        //recursively checks all items in array vs first item
        for (int i=1; i< gpsData.size(); i++){
            distance = distanceBetweenPoints(gpsData.get(0), gpsData.get(i));           //check distance between first point and current point
            if (distance<=50.0){                                                        //if distance is within range increment counter
                numInRange++;
            }
        }
        percentageInRange = ((double)numInRange)/totalNo;                   //find percentage of points within range of first
        if (percentageInRange > 0.8){                                   //if percentage high enough return true
            stopDetected = true;
        }
        return stopDetected;
    }




    private static boolean detectLeave(PotentialStop potS, List<GPSRecord> gpsData){
        boolean stillStopped = false;
        double distance;
                                            //recursively check all points in sublist vs location in potentialstop
        for (int i=0; i<gpsData.size();i++){
            distance = distanceBetweenPoints(potS,gpsData.get(i));      //check distance
            if (distance<= 50.0){                                       //if within range set stillStopped to true
                stillStopped = true;                                    //can only return false if all points are out of range
            }}
        return stillStopped;
    }


    //methods to extract lat and lon values

    private static double distanceBetweenPoints(GPSRecord gps1, GPSRecord gps2){
        double dist = distance(gps1.getLatitude(), gps1.getLongitude(), gps2.getLatitude(), gps2.getLongitude());
        return dist;
    }

    private static double distanceBetweenPoints(PotentialStop p, GPSRecord gps){
        double dist = distance(p.getLatitude(), p.getLongitude(), gps.getLatitude(), gps.getLongitude());
        return dist;
    }


    //haversine formula to detect distance between points on a sphere
    //returns double value in meters

    public static double distance(double latstart, double lonstart, double latfin, double lonfin) {
        double earthRadius = 6371000; //value in meters
        double distLng = Math.toRadians(lonfin-lonstart);
        double distLat = Math.toRadians(latfin-latstart);

        double a = Math.pow((Math.sin(distLat/2)),2) +
                Math.cos(Math.toRadians(latstart)) * Math.cos(Math.toRadians(latfin)) *
                        Math.pow((Math.sin(distLng/2)),2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    //methods to determine average of lat and lon from given points

    private static double averageOfLat(List<GPSRecord> gps){
        double average;
        double total=0.0;
        int counter =0;
        for (GPSRecord g: gps){
            total = total+g.getLatitude();
            counter++;
        }
        average = total/counter;
        return average;

    }

    private static double averageOfLon(List<GPSRecord> gps){
        double average;
        double total=0.0;
        int counter =0;
        for (GPSRecord g: gps){
            total = total+g.getLongitude();
            counter++;
        }
        average = total/counter;
        return average;

    }


}
