package com.example.carworkshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ServiceTypeAdapter extends RecyclerView.Adapter <ServiceTypeAdapter.ServiceTypeViewHolder>{

    private final Context context;
    private final List<Icon> icon;

    public ServiceTypeAdapter(Context context, List<Icon> icon) {
        this.context = context;
        this.icon = icon;
    }


    @NonNull
    @Override
    public ServiceTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_type, parent, false);
        return new ServiceTypeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceTypeViewHolder holder, int position) {
        final Icon iconCurrent = icon.get(position);
        Picasso.get().load(iconCurrent.getiUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.serviceImage);
        holder.serviceName.setText(iconCurrent.getiName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchServiceActivity.class);
                intent.putExtra("iName", iconCurrent.getiName());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return icon.size();
    }

    public static class ServiceTypeViewHolder extends RecyclerView.ViewHolder {

        public ImageView serviceImage;
        public TextView serviceName;

        public ServiceTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.service_image);
            serviceName = itemView.findViewById(R.id.service_name);
        }

    }
}
