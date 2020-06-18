package com.example.ramin.driver.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertTripsModel {

    @SerializedName("distance")
    @Expose
    private int distance;

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("date_of_movement")
    @Expose
    private String date_of_movement;

    @SerializedName("time_of_movement")
    @Expose
    private String time_of_movement;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("driver_id")
    @Expose
    private int driver_id;

    @SerializedName("stopping")
    @Expose
    private String stopping;

    @SerializedName("empty_chair_count")
    @Expose
    private int empty_chair_count;

    @SerializedName("shipment_capacity")
    @Expose
    private String shipment_capacity;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("origin_min_lng")
    @Expose
    private double minLng;

    @SerializedName("origin_min_lat")
    @Expose
    private double minLat;

    @SerializedName("destination_max_lng")
    @Expose
    private double maxLng;

    @SerializedName("destination_max_lat")
    @Expose
    private double maxLat;

    public double getMinLng() {
        return minLng;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMaxLng() {
        return maxLng;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public String getPath() {
        return path;
    }

    public String getResponse() {
        return response;
    }

    public int getDistance() {
        return distance;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate_of_movement() {
        return date_of_movement;
    }

    public String getTime_of_movement() {
        return time_of_movement;
    }

    public int getPrice() {
        return price;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public String getStopping() {
        return stopping;
    }

    public int getEmpty_chair_count() {
        return empty_chair_count;
    }

    public String getShipment_capacity() {
        return shipment_capacity;
    }
}
