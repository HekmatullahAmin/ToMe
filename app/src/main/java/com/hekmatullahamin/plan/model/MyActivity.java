package com.hekmatullahamin.plan.model;

import java.io.Serializable;

public class MyActivity implements Serializable {
    private int activityId, activityTagColor, activityTagImage;
    private String activityDescription, activityFromTime, activityToTime, activityDayOfTheWeek, activityTagName;
    private Long activityFromTimeLong;

    public MyActivity() {
    }

    public MyActivity(int activityId, int activityTagColor, int activityTagImage, String activityDescription,
                      String activityFromTime, String activityToTime, String activityDayOfTheWeek, String activityTagName,
                      Long activityFromTimeLong) {
        this.activityId = activityId;
        this.activityTagColor = activityTagColor;
        this.activityTagImage = activityTagImage;
        this.activityDescription = activityDescription;
        this.activityFromTime = activityFromTime;
        this.activityToTime = activityToTime;
        this.activityDayOfTheWeek = activityDayOfTheWeek;
        this.activityTagName = activityTagName;
        this.activityFromTimeLong = activityFromTimeLong;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
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

    public String getActivityTagName() {
        return activityTagName;
    }

    public void setActivityTagName(String activityTagName) {
        this.activityTagName = activityTagName;
    }

    public int getActivityTagColor() {
        return activityTagColor;
    }

    public void setActivityTagColor(int activityTagColor) {
        this.activityTagColor = activityTagColor;
    }

    public int getActivityTagImage() {
        return activityTagImage;
    }

    public void setActivityTagImage(int activityTagImage) {
        this.activityTagImage = activityTagImage;
    }

    public Long getActivityFromTimeLong() {
        return activityFromTimeLong;
    }

    public void setActivityFromTimeLong(Long activityFromTimeLong) {
        this.activityFromTimeLong = activityFromTimeLong;
    }
}
