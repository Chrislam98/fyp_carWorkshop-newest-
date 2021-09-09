package com.example.carworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserViewOwnerServiceAdapter extends RecyclerView.Adapter<UserViewOwnerServiceAdapter.UserViewOwnerServiceViewHolder> {

    View view;
    Context c;
    ArrayList<Owner_service> ownerServiceArrayList;
    String RM = "RM";

    public UserViewOwnerServiceAdapter(Context c, ArrayList<Owner_service> ownerServiceArrayList) {
        this.c = c;
        this.ownerServiceArrayList = ownerServiceArrayList;
    }

    @NonNull
    @Override
    public UserViewOwnerServiceAdapter.UserViewOwnerServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(c).inflate(R.layout.owner_service_list,parent,false);
        return new UserViewOwnerServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewOwnerServiceAdapter.UserViewOwnerServiceViewHolder holder, int position) {
        final Owner_service ownerService = ownerServiceArrayList.get(position);
        holder.servicePrice.setText(ownerService.getPrice());
        holder.serviceName.setText(ownerService.getServicename());
        holder.serviceTime.setText(ownerService.getTime());
        holder.serviceOwnerId.setText(ownerService.getOwnerId());
    }

    @Override
    public int getItemCount() {
        return ownerServiceArrayList.size();
    }

    public static class UserViewOwnerServiceViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName, servicePrice, serviceTime, serviceOwnerId;

        public UserViewOwnerServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.serviceName);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceTime = itemView.findViewById(R.id.serviceTime);
            serviceOwnerId = itemView.findViewById(R.id.serviceOwnerId);
        }
    }
}
