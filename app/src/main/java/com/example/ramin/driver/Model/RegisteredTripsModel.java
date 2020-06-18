package com.example.ramin.driver.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisteredTripsModel {

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

    @SerializedName("trip_id")
    @Expose
    private int tripId;

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

    public int getTripId() {
        return tripId;
    }
}
