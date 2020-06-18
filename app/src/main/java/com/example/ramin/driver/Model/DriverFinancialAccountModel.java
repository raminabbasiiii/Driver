package com.example.ramin.driver.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverFinancialAccountModel {

    @SerializedName("dept")
    @Expose
    private int dept;

    @SerializedName("recruitment")
    @Expose
    private int recruitment;

    public int getRecruitment() {
        return recruitment;
    }

    public int getDept() {
        return dept;
    }

    public void setDept(int dept) {
        this.dept = dept;
    }
}
