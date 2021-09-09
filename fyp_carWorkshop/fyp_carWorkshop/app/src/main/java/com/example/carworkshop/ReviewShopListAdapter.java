package com.example.carworkshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewShopListAdapter extends RecyclerView.Adapter<ReviewShopListAdapter.ReviewShopListViewHolder>{

    View view;
    ArrayList<Owner> owners;

    public ReviewShopListAdapter(ArrayList<Owner> owners) {
        this.owners = owners;
    }

    @NonNull
    @Override
    public ReviewShopListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list,parent,false);
        return new ReviewShopListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewShopListViewHolder holder, int position) {
        ReviewShopListViewHolder reviewShopListViewHolder = (ReviewShopListViewHolder)holder;
        final Owner owner = owners.get(position);

        reviewShopListViewHolder.ownerId.setText(owner.getOwnerId());
        reviewShopListViewHolder.workshopName.setText(owner.getWorkshopName());
        reviewShopListViewHolder.workshopAddress.setText(owner.getWorkshopAddress());
        reviewShopListViewHolder.appointmentNum.setText(owner.getAppointmentNum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReviewOwnerActivity.class);
                intent.putExtra("ownerId", owner.getOwnerId());
                intent.putExtra("workshopAddress", owner.getWorkshopAddress());
                intent.putExtra("workshopName", owner.getWorkshopName());
                intent.putExtra("appointmentNum", owner.getAppointmentNum());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return owners.size();
    }

    public static class ReviewShopListViewHolder extends RecyclerView.ViewHolder {

        TextView workshopName, workshopAddress, appointmentNum, ownerId;

        public ReviewShopListViewHolder(@NonNull View itemView) {
            super(itemView);

            workshopName = (TextView) itemView.findViewById(R.id.shopName);
            workshopAddress = (TextView) itemView.findViewById(R.id.shopAddress);
            appointmentNum = (TextView) itemView.findViewById(R.id.shopPhone);
            ownerId = (TextView) itemView.findViewById(R.id.ownerId);

        }
    }
}
