package com.hekmatullahamin.plan.model;

public class Plan {
    private int planId, planState = 0;
    private long planDate, planDateAndTime, planNotificationId;
    private String planNote;

    public Plan() {
    }

    public Plan(int planId, int planState, long planDate, long planDateAndTime, long planNotificationId, String planNote) {
        this.planId = planId;
        this.planState = planState;
        this.planDate = planDate;
        this.planDateAndTime = planDateAndTime;
        this.planNotificationId = planNotificationId;
        this.planNote = planNote;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getPlanState() {
        return planState;
    }

    public void setPlanState(int planState) {
        this.planState = planState;
    }

    public long getPlanDate() {
        return planDate;
    }

    public void setPlanDate(long planDate) {
        this.planDate = planDate;
    }

    public long getPlanNotificationId() {
        return planNotificationId;
    }

    public void setPlanNotificationId(long planNotificationId) {
        this.planNotificationId = planNotificationId;
    }

    public String getPlanNote() {
        return planNote;
    }

    public void setPlanNote(String planNote) {
        this.planNote = planNote;
    }

    public long getPlanDateAndTime() {
        return planDateAndTime;
    }

    public void setPlanDateAndTime(long planDateAndTime) {
        this.planDateAndTime = planDateAndTime;
    }
}
