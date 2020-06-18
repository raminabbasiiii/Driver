package com.example.ramin.driver.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.driver.Model.TripsModel;
import com.example.ramin.driver.Network.GetDriverData;
import com.example.ramin.driver.Network.RetrofitDriverInstance;
import com.example.ramin.driver.Preferences;
import com.example.ramin.driver.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerTripsAdapter extends RecyclerView.Adapter<RecyclerTripsAdapter.MyViewHolder>{

    private List<TripsModel> tripsModelList;
    private Context context;
    private static final String TAG ="TAG";

    public RecyclerTripsAdapter(List<TripsModel> tripsList, Context context) {
        this.tripsModelList = tripsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trips_recycler_list_item,parent,false);
        return new MyViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(final RecyclerTripsAdapter.MyViewHolder holder, final int position) {

        TripsModel model = tripsModelList.get(position);

        Preferences p = new Preferences(context);
        int dId = p.getDriverId();
        int tripId = model.getTripId();

        GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
        Call<TripsModel> call = api.getPassengerCount(dId,tripId);
        call.enqueue(new Callback<TripsModel>() {
            @Override
            public void onResponse(Call<TripsModel> call, Response<TripsModel> response) {
                if (response.isSuccessful()) {
                    TripsModel model = response.body();
                    holder.countPassengerTrips.setText(String.valueOf(model.getPassengerCount()));
                } else {
                    Log.i(TAG, "onTripsResponse: " + response.code() + " " + response.message());
                    Toast.makeText(context, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TripsModel> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(context, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
            }
        });

        String dateTime = model.getDataOfDoTrip() + "       " + model.getTimeOfDoTrip();

        holder.originTrips.setText(model.getOrigin());
        holder.destinationTrips.setText(model.getDestination());
        holder.doDateTrips.setText(dateTime);
        holder.tripIdTrips.setText(String.valueOf(tripId));
    }

    @Override
    public int getItemCount() {
        return tripsModelList.size();
    }

    public void updateTripsList(List<TripsModel> modelList) {
        this.tripsModelList = modelList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView originTrips;
        private TextView destinationTrips;
        private TextView doDateTrips;
        private TextView tripIdTrips;
        private TextView countPassengerTrips;


        public MyViewHolder(View itemView) {
            super(itemView);

            originTrips = itemView.findViewById(R.id.tv_origin_trips);
            destinationTrips = itemView.findViewById(R.id.tv_destination_trips);
            doDateTrips = itemView.findViewById(R.id.tv_date_doing_trips);
            tripIdTrips = itemView.findViewById(R.id.tv_trip_id_trips);
            countPassengerTrips = itemView.findViewById(R.id.tv_count_passenger);
        }
    }
}
