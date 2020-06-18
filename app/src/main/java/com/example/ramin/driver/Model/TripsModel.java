package com.example.ramin.driver.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripsModel {


    @SerializedName("trip_id")
    @Expose
    private int tripId;

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("data_of_do_trip")
    @Expose
    private String dataOfDoTrip;

    @SerializedName("time_of_do_trip")
    @Expose
    private String timeOfDoTrip;

    @SerializedName("passenger_count")
    @Expose
    private int passengerCount;

    public int getTripId() {
        return tripId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDataOfDoTrip() {
        return dataOfDoTrip;
    }

    public String getTimeOfDoTrip() {
        return timeOfDoTrip;
    }

    public int getPassengerCount() {
        return passengerCount;
    }
}
