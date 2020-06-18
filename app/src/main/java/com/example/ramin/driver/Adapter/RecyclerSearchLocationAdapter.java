package com.example.ramin.driver.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ramin.driver.Model.SearchMap.Item;
import com.example.ramin.driver.Model.SearchMap.Location;
import com.example.ramin.driver.R;


import java.util.List;

public class RecyclerSearchLocationAdapter extends RecyclerView.Adapter<RecyclerSearchLocationAdapter.ViewHolder>{

    private List<Item> items;
    private OnSearchItemListener onSearchItemListener;

    public RecyclerSearchLocationAdapter(List<Item> items, OnSearchItemListener onSearchItemListener) {
        this.items = items;
        this.onSearchItemListener = onSearchItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_location_recycler_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(items.get(position).getTitle());
        holder.tvAddress.setText(items.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<Item> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_search);
            tvAddress = itemView.findViewById(R.id.tv_address_search);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String title = items.get(getAdapterPosition()).getTitle();
            Location location = items.get(getAdapterPosition()).getLocation();
            //LngLat lngLat = new LngLat(location.getX() , location.getY());
            double lat = location.getX();
            double lng = location.getY();
            onSearchItemListener.onSearchItemClick(lat,lng,title);
        }
    }

    public interface OnSearchItemListener{
        void onSearchItemClick(double lat,double lng, String title);
    }
}
