package com.example.ramin.driver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

public class RecyclerTransactionOfTripAdapter extends RecyclerView.Adapter<RecyclerTransactionOfTripAdapter.MyViewHolder>{

    List<TransactionOfTripModel> transaction;
    Context context;

    public RecyclerTransactionOfTripAdapter(List<TransactionOfTripModel> passList, Context context) {
        this.transaction = passList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_of_trip_recycler_list_item,parent,false);
        return new MyViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        TransactionOfTripModel model = transaction.get(position);

        holder.origin.setText(model.getOrigin());
        holder.destination.setText(model.getDestination());
        holder.doDate.setText(model.getDoDate());
        holder.tripId.setText(model.getTripId());
        holder.receivedMoney.setText(model.getReceivedMoney());
    }

    @Override
    public int getItemCount() {
        return transaction.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

         public TextView origin;
         public TextView destination;
         public TextView doDate;
         public TextView doTime;
         public TextView tripId;
         public TextView receivedMoney;


        public MyViewHolder(View itemView) {
            super(itemView);

            origin = itemView.findViewById(R.id.tv_origin_transaction);
            destination = itemView.findViewById(R.id.tv_destination_transaction);
            doDate = itemView.findViewById(R.id.tv_date_doing_transaction);
            tripId = itemView.findViewById(R.id.tv_trip_id_transaction);
            receivedMoney = itemView.findViewById(R.id.tv_amount_of_money_transaction);
        }
    }
}
