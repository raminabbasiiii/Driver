package com.example.ramin.driver.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ramin.driver.Model.PassengersListModel;
import com.example.ramin.driver.R;

import java.util.List;

public class RecyclerPassengersListAdapter extends RecyclerView.Adapter<RecyclerPassengersListAdapter.MyViewHolder> {

    private List<PassengersListModel> passList;
    private Context context;

    public RecyclerPassengersListAdapter(List<PassengersListModel> passList, Context context) {
        this.passList = passList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.passengers_list_recycler_list_item, parent, false);
        return new MyViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final PassengersListModel model = passList.get(position);

        String name = model.getPassengerName() + " " + model.getPassengerFamily();
        String dateTime = model.getDateOfMovement() + "     " + model.getTimeOfMovement();

        holder.namePassengers.setText(name);
        holder.destinationPassengers.setText(model.getDestination());
        holder.originPassengers.setText(model.getOrigin());
        holder.mobilePassengers.setText(model.getPassengerMobile());
        holder.tvDateTime.setText(dateTime);
        holder.tvChairReservedCount.setText(String.valueOf(model.getChairReservedCount()));

        holder.btnCallPassengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + model.getPassengerMobile()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(call);
            }
        });
    }

    @Override
    public int getItemCount() {
        return passList.size();
    }

    public void updatePasengerList(List<PassengersListModel> listModels) {
        this.passList = listModels;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView originPassengers;
        private TextView destinationPassengers;
        private TextView namePassengers;
        private TextView mobilePassengers;
        private TextView tvDateTime,tvChairReservedCount;
        private ImageButton btnCallPassengers;

        public MyViewHolder(View itemView) {
            super(itemView);

            originPassengers = itemView.findViewById(R.id.tv_origin_passengers_list);
            destinationPassengers = itemView.findViewById(R.id.tv_destination_passengers_list);
            namePassengers = itemView.findViewById(R.id.tv_name_passengers_list);
            mobilePassengers = itemView.findViewById(R.id.tv_mobile_passengers_list);
            btnCallPassengers = itemView.findViewById(R.id.btn_call_passenger_list);
            tvDateTime = itemView.findViewById(R.id.tv_date_movement);
            tvChairReservedCount = itemView.findViewById(R.id.tv_chair_reserve_count);
        }
    }
}
