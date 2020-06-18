package com.example.ramin.driver.Network;

import com.example.ramin.driver.Model.DriverFinancialAccountModel;
import com.example.ramin.driver.Model.LoginModel;
import com.example.ramin.driver.Model.PassengersListModel;
import com.example.ramin.driver.Model.RegisteredTripsModel;
import com.example.ramin.driver.Model.InsertTripsModel;
import com.example.ramin.driver.Model.TripsModel;
import com.example.ramin.driver.Model.TransactionOfSalaryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDriverData {

    @GET("login.php")
    Call<LoginModel> getDriverLogin(@Query("driver_username") String userName,@Query("driver_password") String password);

    @GET("RegisteredTrips.php")
    Call<List<RegisteredTripsModel>> getRegisteredTrips(@Query("driver_id") int driverId);

    @GET("insertTrip.php")
    Call<InsertTripsModel> insertTrips(@Query("distance") int distance, @Query("origin") String origin, @Query("destination") String destination, @Query("date_of_movement") String date, @Query("time_of_movement") String time, @Query("price") int price, @Query("driver_id") int driverId, @Query("stopping") String stopping, @Query("empty_chair_count") int chairCount, @Query("shipment_capacity") String capacity);

    @GET("cancelTrip.php")
    Call<InsertTripsModel> cancelTrip(@Query("trip_id") int tripId);

    @GET("selectPassengerList.php")
    Call<List<PassengersListModel>> getPassengerList(@Query("driver_id") int driverId);

    @GET("selectTrips.php")
    Call<List<TripsModel>> getTripsList(@Query("driver_id") int driverId);

    @GET("passengerCount.php")
    Call<TripsModel> getPassengerCount(@Query("driver_id") int driverId, @Query("trip_id") int tripId);

    @GET("selcetDept.php")
    Call<DriverFinancialAccountModel> getDept(@Query("driver_id") int driverId, @Query("date") String date);

    @GET("selectRecruitment.php")
    Call<DriverFinancialAccountModel> getRecruitment(@Query("driver_id") int driverId, @Query("date") String date);

    @GET("selectTransactionSalary.php")
    Call<List<TransactionOfSalaryModel>> getTransactionSalary (@Query("driver_id") int driverId);
}
