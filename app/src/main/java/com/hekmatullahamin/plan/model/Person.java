package com.hekmatullahamin.plan.model;

import java.io.Serializable;

public class Person implements Serializable {
    private int personId;
    private double personMoneyAmount;
    private String personMoneyGainOrLoss, personName;

    public Person() {
    }

    public Person(int personId, String personName) {
        this.personId = personId;
        this.personName = personName;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public double getPersonMoneyAmount() {
        return personMoneyAmount;
    }

    public void setPersonMoneyAmount(double personMoneyAmount) {
        this.personMoneyAmount = personMoneyAmount;
    }

    public String getPersonMoneyGainOrLoss() {
        return personMoneyGainOrLoss;
    }

    public void setPersonMoneyGainOrLoss(String personMoneyGainOrLoss) {
        this.personMoneyGainOrLoss = personMoneyGainOrLoss;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
