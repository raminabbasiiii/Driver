package com.example.ramin.driver.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ramin.driver.Adapter.RecyclerRegisteredTripsAdapter;
import com.example.ramin.driver.Model.RegisteredTripsModel;
import com.example.ramin.driver.Network.GetDriverData;
import com.example.ramin.driver.Network.RetrofitDriverInstance;
import com.example.ramin.driver.Preferences;
import com.example.ramin.driver.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisteredTripsFragment extends Fragment {

    View root;
    RecyclerView registeredTripsRecycler;
    RecyclerRegisteredTripsAdapter adapter;
    List<RegisteredTripsModel> registeredTripsModelList = new ArrayList<>();
    private static final String TAG ="TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_registered_trips, container, false);

        setRecyclerView();
        getRegisteredTripsFromServer();
        return root;
    }

    private void setRecyclerView() {
        registeredTripsRecycler = root.findViewById(R.id.registered_trips_recycler);
        adapter = new RecyclerRegisteredTripsAdapter(registeredTripsModelList,getContext());
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(getContext());
        registeredTripsRecycler.setLayoutManager(regLayoutManager);
        registeredTripsRecycler.setItemAnimator(new DefaultItemAnimator());
        registeredTripsRecycler.setHasFixedSize(true);
        registeredTripsRecycler.setAdapter(adapter);
    }

    private void getRegisteredTripsFromServer () {

        Preferences preferences = new Preferences(getContext());

        GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
        Call<List<RegisteredTripsModel>> call = api.getRegisteredTrips(preferences.getDriverId());
        call.enqueue(new Callback<List<RegisteredTripsModel>>() {
            @Override
            public void onResponse(Call<List<RegisteredTripsModel>> call, Response<List<RegisteredTripsModel>> response) {
                if (response.isSuccessful()) {
                    Log.i("ramin","getResponse");
                    registeredTripsModelList = response.body();
                    adapter.updateRegisteredTripList(registeredTripsModelList);
                } else {
                    Log.i(TAG, "onRegisteredResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RegisteredTripsModel>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
