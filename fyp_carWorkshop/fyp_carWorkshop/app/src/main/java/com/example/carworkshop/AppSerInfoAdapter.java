package com.example.carworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppSerInfoAdapter extends RecyclerView.Adapter<AppSerInfoAdapter.MyViewHolder> {
    Context context;
    List<Owner_service> owner_services;

    public AppSerInfoAdapter(Context c1, List<Owner_service> osw){
        context = c1;
        owner_services = osw;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.appserlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.desername.setText(owner_services.get(position).getServicename());
        holder.deserprice.setText(owner_services.get(position).getPrice());
        holder.desertime.setText(owner_services.get(position).getTime());


    }

    @Override
    public int getItemCount() {
        return owner_services.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView desername,deserprice,desertime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            desername = (TextView) itemView.findViewById(R.id.desername);
            deserprice = (TextView) itemView.findViewById(R.id.deserprice);
            desertime = (TextView) itemView.findViewById(R.id.desertime);
        }
    }
}
