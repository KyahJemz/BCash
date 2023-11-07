package com.sscr.bcash;

public class TransactionModel {
    String transactionDate;
    String transactionAmount;
    String transactionAddress;
    String transactionType;

    public String getTransactionDate() {
        return transactionDate;
    }
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
    public String getTransactionAmount() {
        return transactionAmount;
    }
    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
    public String getTransactionAddress() {
        return transactionAddress;
    }
    public void setTransactionAddress(String transactionAddress) {
        this.transactionAddress = transactionAddress;
    }
    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionModel(String transactionAddress, String transactionAmount, String transactionDate, String transactionType) {
        this.transactionAddress = transactionAddress;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
    }


}
