package com.example.ramin.driver.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ramin.driver.R;
import com.example.ramin.driver.Model.TransactionOfSalaryModel;

import java.text.DecimalFormat;
import java.util.List;

public class RecyclerTransactionOfSalaryAdapter extends RecyclerView.Adapter<RecyclerTransactionOfSalaryAdapter.MyViewHolder>{

    private List<TransactionOfSalaryModel> transaction;
    private Context context;

    public RecyclerTransactionOfSalaryAdapter(List<TransactionOfSalaryModel> passList, Context context) {
        this.transaction = passList;
        this.context = context;
    }

    @Override
    public RecyclerTransactionOfSalaryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View aView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_of_salary_recycler_list_item,parent,false);
        return new MyViewHolder(aView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        TransactionOfSalaryModel model = transaction.get(position);

        holder.dateSalary.setText(model.getDateOfReceiveSalary());
        int sal = model.getSalary();
        DecimalFormat d = new DecimalFormat("###,###,###");
        String rial = String.valueOf(d.format(sal)) + " " + "ریال";
        holder.moneySalary.setText(rial);
    }

    public void  updateTransactionSalaryList(List<TransactionOfSalaryModel> models) {
        this.transaction = models;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return transaction.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView moneySalary;
        private TextView dateSalary;

        public MyViewHolder(View itemView) {
            super(itemView);

            moneySalary = itemView.findViewById(R.id.tv_money_salary);
            dateSalary = itemView.findViewById(R.id.tv_date_salary);
        }
    }
}
