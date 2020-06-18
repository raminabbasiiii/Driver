package com.example.ramin.driver.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassengersListModel {

    @SerializedName("passenger_name")
    @Expose
    private String passengerName;

    @SerializedName("passenger_family")
    @Expose
    private String passengerFamily;

    @SerializedName("passenger_mobile")
    @Expose
    private String passengerMobile;

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("date_of_movement")
    @Expose
    private String dateOfMovement;

    @SerializedName("time_of_movement")
    @Expose
    private String timeOfMovement;

    @SerializedName("chair_reserved_count")
    @Expose
    private int chairReservedCount;

    public int getChairReservedCount() {
        return chairReservedCount;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getPassengerFamily() {
        return passengerFamily;
    }

    public String getPassengerMobile() {
        return passengerMobile;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDateOfMovement() {
        return dateOfMovement;
    }

    public String getTimeOfMovement() {
        return timeOfMovement;
    }

}
