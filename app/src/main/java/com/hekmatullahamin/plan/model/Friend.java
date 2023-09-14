package com.hekmatullahamin.plan.model;

import java.io.Serializable;

public class Friend implements Serializable {
    private int friendId;
    private double friendMoneyAmount;
    private String friendMoneyGainOrLoss, friendName;

    public Friend() {
    }

    public Friend(int friendId, String friendName) {
        this.friendId = friendId;
        this.friendName = friendName;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public double getFriendMoneyAmount() {
        return friendMoneyAmount;
    }

    public void setFriendMoneyAmount(double friendMoneyAmount) {
        this.friendMoneyAmount = friendMoneyAmount;
    }

    public String getFriendMoneyGainOrLoss() {
        return friendMoneyGainOrLoss;
    }

    public void setFriendMoneyGainOrLoss(String friendMoneyGainOrLoss) {
        this.friendMoneyGainOrLoss = friendMoneyGainOrLoss;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
