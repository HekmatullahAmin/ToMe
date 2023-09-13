package com.hekmatullahamin.plan.model;

public class MyActivity {
    private int activityId;
    private String activityName, activityFromTime, activityToTime, activityDayOfTheWeek;

    public MyActivity() {
    }

    public MyActivity(int activityId, String activityName, String activityFromTime, String activityToTime, String activityDayOfTheWeek) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityFromTime = activityFromTime;
        this.activityToTime = activityToTime;
        this.activityDayOfTheWeek = activityDayOfTheWeek;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityFromTime() {
        return activityFromTime;
    }

    public void setActivityFromTime(String activityFromTime) {
        this.activityFromTime = activityFromTime;
    }

    public String getActivityToTime() {
        return activityToTime;
    }

    public void setActivityToTime(String activityToTime) {
        this.activityToTime = activityToTime;
    }

    public String getActivityDayOfTheWeek() {
        return activityDayOfTheWeek;
    }

    public void setActivityDayOfTheWeek(String activityDayOfTheWeek) {
        this.activityDayOfTheWeek = activityDayOfTheWeek;
    }
}
