package com.hekmatullahamin.plan.model;

public class Item {
    private int itemId, itemFromPersonId, itemState;
    private double itemMoneyAmount = 0.0;
    private Long itemAddedDate;
    private String itemName, itemMoneyType;


    public Item() {
    }

    public Item(int itemId, int itemFromPersonId, int itemState, double itemMoneyAmount, Long itemAddedDate, String itemName, String itemMoneyType) {
        this.itemId = itemId;
        this.itemFromPersonId = itemFromPersonId;
        this.itemState = itemState;
        this.itemMoneyAmount = itemMoneyAmount;
        this.itemAddedDate = itemAddedDate;
        this.itemName = itemName;
        this.itemMoneyType = itemMoneyType;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemFromPersonId() {
        return itemFromPersonId;
    }

    public void setItemFromPersonId(int itemFromPersonId) {
        this.itemFromPersonId = itemFromPersonId;
    }

    public int getItemState() {
        return itemState;
    }

    public void setItemState(int itemState) {
        this.itemState = itemState;
    }

    public double getItemMoneyAmount() {
        return itemMoneyAmount;
    }

    public void setItemMoneyAmount(double itemMoneyAmount) {
        this.itemMoneyAmount = itemMoneyAmount;
    }

    public Long getItemAddedDate() {
        return itemAddedDate;
    }

    public void setItemAddedDate(Long itemAddedDate) {
        this.itemAddedDate = itemAddedDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemMoneyType() {
        return itemMoneyType;
    }

    public void setItemMoneyType(String itemMoneyType) {
        this.itemMoneyType = itemMoneyType;
    }
}
