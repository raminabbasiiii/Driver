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

import com.example.ramin.driver.Adapter.RecyclerPassengersListAdapter;
import com.example.ramin.driver.Model.PassengersListModel;
import com.example.ramin.driver.Network.GetDriverData;
import com.example.ramin.driver.Network.RetrofitDriverInstance;
import com.example.ramin.driver.Preferences;
import com.example.ramin.driver.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengersListFragment extends Fragment {

    View root;
    RecyclerView passengersListRecycler;
    RecyclerPassengersListAdapter adapter;
    List<PassengersListModel> modelList = new ArrayList<>();
    private static final String TAG ="TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_passengers_list, container, false);

        setRecyclerView();
        getPassengerListFromServer();
        return root;
    }

    private void setRecyclerView() {
        passengersListRecycler = root.findViewById(R.id.passengers_list_recycler);
        adapter = new RecyclerPassengersListAdapter(modelList,getContext());
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(getContext());
        passengersListRecycler.setLayoutManager(regLayoutManager);
        passengersListRecycler.setItemAnimator(new DefaultItemAnimator());
        passengersListRecycler.setHasFixedSize(true);
        passengersListRecycler.setAdapter(adapter);
    }

    private void getPassengerListFromServer() {
        Preferences preferences = new Preferences(getContext());
        int dId = preferences.getDriverId();
        GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
        Call<List<PassengersListModel>> call = api.getPassengerList(dId);
        call.enqueue(new Callback<List<PassengersListModel>>() {
            @Override
            public void onResponse(Call<List<PassengersListModel>> call, Response<List<PassengersListModel>> response) {
                if (response.isSuccessful()) {
                    Log.i("ramin","getResponse");
                    modelList = response.body();
                    adapter.updatePasengerList(modelList);
                } else {
                    Log.i(TAG, "onPassengerListResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PassengersListModel>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}