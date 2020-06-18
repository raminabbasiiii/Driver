package com.example.ramin.driver;

public class TransactionOfTripModel {

    private String origin;
    private String destination;
    private String doDate;
    private String receivedMoney;
    private String tripId;
    private String doTime;

    public TransactionOfTripModel(String origin, String destination, String date, String receivedMoney, String tripId, String time) {
        this.origin = origin;
        this.destination = destination;
        this.doDate = date;
        this.receivedMoney = receivedMoney;
        this.tripId = tripId;
        this.doTime = time;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getReceivedMoney() {
        return receivedMoney;
    }

    public String getTripId() {
        return tripId;
    }

    public String getDoDate() {
        return doDate;
    }

    public String getDoTime() {
        return doTime;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }

    public void setReceivedMoney(String receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setDoTime(String doTime) {
        this.doTime = doTime;
    }
}
