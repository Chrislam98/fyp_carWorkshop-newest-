package com.example.carworkshop;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OwnerAdapter extends RecyclerView.Adapter<OwnerAdapter.OwnerViewHolder> {

    ArrayList<Owner> ownerArrayList;
    View v;
    /*Context context;
    public OwnerAdapter(Context c ,ArrayList<Owner> owner) {
        this.context = c
        this.ownerArrayList = owner;
    }*/

    public OwnerAdapter(ArrayList<Owner> owner) {
        this.ownerArrayList = owner;
    }


    @NonNull
    @Override
    public OwnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list, parent, false);
        return new OwnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerViewHolder holder, int position) {
        OwnerViewHolder ownerViewHolder = (OwnerViewHolder)holder;
        final Owner owner = ownerArrayList.get(position);

        ownerViewHolder.ownerId.setText(owner.getOwnerId());
        ownerViewHolder.workshopName.setText(owner.getWorkshopName());
        ownerViewHolder.workshopAddress.setText(owner.getWorkshopAddress());
        ownerViewHolder.appointmentNum.setText(owner.getAppointmentNum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailsOwnerActivity.class);
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
        if(ownerArrayList != null){
            return ownerArrayList.size();
        } else {
            return 0;
        }

    }

    static class OwnerViewHolder extends RecyclerView.ViewHolder {

        TextView workshopName, workshopAddress, appointmentNum, ownerId;
        public OwnerViewHolder(@NonNull View itemView) {
            super(itemView);

            workshopName = (TextView) itemView.findViewById(R.id.shopName);
            workshopAddress = (TextView) itemView.findViewById(R.id.shopAddress);
            appointmentNum = (TextView) itemView.findViewById(R.id.shopPhone);
            ownerId = (TextView) itemView.findViewById(R.id.ownerId);
        }

    }
}
