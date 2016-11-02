package com.example.tonyquick.testapp.models;

/**
 * Created by Tony Quick on 28/03/2016.
 */
public class ActivityRecord {
    private long _id;
    private long locationid;
    private long activityid;
    private String date;
    private String startTime;
    private String finishTime;

    //method to define activity representing individual database record

    public ActivityRecord(long _id, long locationid, long activityid, String date, String startTime, String finishTime) {
        this._id = _id;
        this.locationid = locationid;
        this.activityid = activityid;
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }


    public ActivityRecord(long locationid, long activityid, String date, String startTime, String finishTime) {
        this.locationid = locationid;
        this.activityid = activityid;
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "ActivityRecord{" +
                "_id=" + _id +
                ", locationid=" + locationid +
                ", activityid=" + activityid +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", finishTime='" + finishTime + '\'' +
                '}';
    }

    public long get_id() {
        return _id;
    }

    public long getLocationid() {
        return locationid;
    }

    public long getActivityid() {
        return activityid;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }
}
