package com.example.ramin.driver.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("driver_id")
    @Expose
    private int driverId;

    @SerializedName("driver_name")
    @Expose
    private String driverName;

    @SerializedName("driver_family")
    @Expose
    private String driverFamily;

    @SerializedName("driver_image")
    @Expose
    private String driverImage;

    @SerializedName("response")
    @Expose
    private String response;

    public String getResponse() {
        return response;
    }

    public int getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverFamily() {
        return driverFamily;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverFamily(String driverFamily) {
        this.driverFamily = driverFamily;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
