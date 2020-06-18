package com.example.ramin.driver.Adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramin.driver.Activity.LoginActivity;
import com.example.ramin.driver.Model.InsertTripsModel;
import com.example.ramin.driver.Model.RegisteredTripsModel;
import com.example.ramin.driver.Network.GetDriverData;
import com.example.ramin.driver.Network.RetrofitDriverInstance;
import com.example.ramin.driver.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerRegisteredTripsAdapter extends RecyclerView.Adapter<RecyclerRegisteredTripsAdapter.MyViewHolder>{

    private List<RegisteredTripsModel> regList;
    private Context context;
    private Button btnPositiveCancelTrip,btnNegativeCancelTrip;
    private static final String TAG ="TAG";

    public RecyclerRegisteredTripsAdapter(List<RegisteredTripsModel> regList, Context context) {
        this.regList = regList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_trips_recycler_list_item,parent,false);
        return new MyViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        RegisteredTripsModel registeredTripsModel = regList.get(position);

        String dateTime = registeredTripsModel.getDateOfMovement() + "      " + registeredTripsModel.getTimeOfMovement();

        String tId = String.valueOf(registeredTripsModel.getTripId());
        holder.tripIdRegistered.setText(tId);
        holder.dateMovementRegistered.setText(dateTime);
        holder.destinationRegistered.setText(registeredTripsModel.getDestination());
        holder.originRegistered.setText(registeredTripsModel.getOrigin());

        holder.btnCancelTripRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //runCancelAlert();
                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CancelAlertDialog);
                builder.setView(R.layout.cancel_alert_dialog)
                        .setCancelable(true);

                final AlertDialog dialog = builder.create();
                dialog.show();

                btnPositiveCancelTrip = dialog.findViewById(R.id.btn_positive_cancel_trip);
                btnNegativeCancelTrip = dialog.findViewById(R.id.btn_negative_cancel_trip);

                btnPositiveCancelTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int tripId = regList.get(holder.getAdapterPosition()).getTripId();
                        GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
                        Call<InsertTripsModel> call = api.cancelTrip(tripId);
                        call.enqueue(new Callback<InsertTripsModel>() {
                            @Override
                            public void onResponse(Call<InsertTripsModel> call, Response<InsertTripsModel> response) {
                                switch (response.body().getResponse()) {
                                    case "DELETED":
                                        Toast.makeText(context, "سفر لغو شد!", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        regList.remove(holder.getAdapterPosition());
                                        notifyDataSetChanged();
                                        break;
                                    case "FAILED":
                                        Toast.makeText(context, "سفر لغو نشد. دوباره امتحان کتید!", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        break;
                                    default:
                                        Log.i(TAG, "onCancelResponse: " + response.code() + " " + response.message());
                                        Toast.makeText(context, "خطا در برقراری ارتباط!", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(Call<InsertTripsModel> call, Throwable t) {
                                Log.i(TAG, "onFailure: " + t.getMessage());
                                Toast.makeText(context, "ارتباط برقرار نشد!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                btnNegativeCancelTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"سفر لغو نشد!!!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return regList.size();
    }

    public void updateRegisteredTripList (List<RegisteredTripsModel> list) {
        this.regList = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView originRegistered;
        private TextView destinationRegistered;
        private TextView dateMovementRegistered;
        private TextView tripIdRegistered;
        private ImageButton btnCancelTripRegistered;

        public MyViewHolder(View itemView) {
            super(itemView);

            originRegistered = itemView.findViewById(R.id.tv_origin_registered);
            destinationRegistered = itemView.findViewById(R.id.tv_destination_registered);
            dateMovementRegistered = itemView.findViewById(R.id.tv_date_movement_registered);
            tripIdRegistered = itemView.findViewById(R.id.tv_trip_id_registered);
            btnCancelTripRegistered = itemView.findViewById(R.id.btn_cancel_trip_registered);
        }
    }

        /*private void runCancelAlert() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CancelAlertDialog);
            builder.setView(R.layout.cancel_alert_dialog)
                    .setCancelable(true);

            final AlertDialog dialog = builder.create();
            dialog.show();

            btnPositiveCancelTrip = dialog.findViewById(R.id.btn_positive_cancel_trip);
            btnNegativeCancelTrip = dialog.findViewById(R.id.btn_negative_cancel_trip);

            btnPositiveCancelTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String p = regList.get()
                    int tripId = Integer.parseInt()
                    GetDriverData api = RetrofitDriverInstance.getRetrofitDriver().create(GetDriverData.class);
                    Call<InsertTripsModel> call = api.cancelTrip()
                    dialog.dismiss();
                }
            });

            btnNegativeCancelTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"سفر لغو نشد!!!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }*/

    }