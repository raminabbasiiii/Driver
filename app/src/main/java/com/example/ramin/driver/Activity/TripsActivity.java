package com.example.ramin.driver.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.driver.Adapter.RecyclerTripsAdapter;
import com.example.ramin.driver.Model.TripsModel;
import com.example.ramin.driver.Network.GetDriverData;
import com.example.ramin.driver.Network.RetrofitDriverInstance;
import com.example.ramin.driver.Preferences;
import com.example.ramin.driver.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TripsActivity extends AppCompatActivity {

    Toolbar tripsToolbar;
    RecyclerView tripsRecycler;
    RecyclerTripsAdapter adapter;
    List<TripsModel> modelList = new ArrayList<>();
    TextView tvToolbarTitle;
    private static final String TAG ="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setToolbar();
        setRecyclerView();
        getTripsListFromServer();
    }

    private void setToolbar() {
        tripsToolbar = findViewById(R.id.trips_toolbar);
        tvToolbarTitle = tripsToolbar.findViewById(R.id.trips_toolbar_title);
        setSupportActionBar(tripsToolbar);
        tvToolbarTitle.setText(R.string.trips_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setRecyclerView() {
        tripsRecycler = findViewById(R.id.trips_recycler);
        adapter = new RecyclerTripsAdapter(modelList,getApplicationContext());
        RecyclerView.LayoutManager regLayoutManager = new LinearLayoutManager(getApplicationContext());
        tripsRecycler.setLayoutManager(regLayoutManager);
        tripsRecycler.setItemAnimator(new DefaultItemAnimator());
        tripsRecycler.setHasFixedSize(true);
        tripsRecycler.setAdapter(adapter);
    }

    private void getTripsListFromServer() {
        Preferences preferences = new Preferences(this);
        int dId = preferences.getDriverId();

        GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
        Call<List<TripsModel>> call = api.getTripsList(dId);
        call.enqueue(new Callback<List<TripsModel>>() {
            @Override
            public void onResponse(Call<List<TripsModel>> call, Response<List<TripsModel>> response) {
                if (response.isSuccessful()) {
                    modelList = response.body();
                    adapter.updateTripsList(modelList);
                } else {
                    Log.i(TAG, "onTripsResponse: " + response.code() + " " + response.message());
                    Toast.makeText(getApplicationContext(), "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TripsModel>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
