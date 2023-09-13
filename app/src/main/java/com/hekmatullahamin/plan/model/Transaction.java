package com.hekmatullahamin.plan.model;

public class Transaction {
    private int transactionItemId;
    private double transactionMoneyAmount;
    private Long transactionAddedDate;
    private String transactionItemName, transactionMoneyType, transactionFromPersonName;

    public Transaction() {
    }

    public Transaction(int transactionItemId, double transactionMoneyAmount, Long transactionAddedDate, String transactionItemName, String transactionMoneyType, String transactionFromPersonName) {
        this.transactionItemId = transactionItemId;
        this.transactionMoneyAmount = transactionMoneyAmount;
        this.transactionAddedDate = transactionAddedDate;
        this.transactionItemName = transactionItemName;
        this.transactionMoneyType = transactionMoneyType;
        this.transactionFromPersonName = transactionFromPersonName;
    }

    public int getTransactionItemId() {
        return transactionItemId;
    }

    public void setTransactionItemId(int transactionItemId) {
        this.transactionItemId = transactionItemId;
    }

    public double getTransactionMoneyAmount() {
        return transactionMoneyAmount;
    }

    public void setTransactionMoneyAmount(double transactionMoneyAmount) {
        this.transactionMoneyAmount = transactionMoneyAmount;
    }

    public Long getTransactionAddedDate() {
        return transactionAddedDate;
    }

    public void setTransactionAddedDate(Long transactionAddedDate) {
        this.transactionAddedDate = transactionAddedDate;
    }

    public String getTransactionItemName() {
        return transactionItemName;
    }

    public void setTransactionItemName(String transactionItemName) {
        this.transactionItemName = transactionItemName;
    }

    public String getTransactionMoneyType() {
        return transactionMoneyType;
    }

    public void setTransactionMoneyType(String transactionMoneyType) {
        this.transactionMoneyType = transactionMoneyType;
    }

    public String getTransactionFromPersonName() {
        return transactionFromPersonName;
    }

    public void setTransactionFromPersonName(String transactionFromPersonName) {
        this.transactionFromPersonName = transactionFromPersonName;
    }
}
