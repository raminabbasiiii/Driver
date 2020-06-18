package com.example.ramin.driver.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionOfSalaryModel {

    @SerializedName("salary")
    @Expose
    private int salary;
    @SerializedName("date_of_receive_salary")
    @Expose
    private String dateOfReceiveSalary;

    public int getSalary() {
        return salary;
    }

    public String getDateOfReceiveSalary() {
        return dateOfReceiveSalary;
    }

}
