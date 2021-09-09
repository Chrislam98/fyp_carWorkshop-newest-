package com.example.carworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DetailAppointmentServiceAdapter extends RecyclerView.Adapter <DetailAppointmentServiceAdapter.DetailAppointmentServiceViewHolder>{

    View view;
    Context c;
    ArrayList<Appointment_service> appointment_services;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    public DetailAppointmentServiceAdapter(Context c, ArrayList<Appointment_service> appointment_services) {
        this.c = c;
        this.appointment_services = appointment_services;
    }

    @NonNull
    @Override
    public DetailAppointmentServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(c).inflate(R.layout.details_service,parent,false);
        return new DetailAppointmentServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAppointmentServiceViewHolder holder, int position) {
        final Appointment_service appointmentService = appointment_services.get(position);
        holder.userId.setText(firebaseUser.getUid());
        holder.serviceName.setText(appointmentService.getServicename());
        holder.servicePrice.setText(appointmentService.getPrice());
        holder.category.setText(appointmentService.getCategory());
        holder.ownerId.setText(appointmentService.getOwnerId());
        holder.time.setText(appointmentService.getTime());
        holder.orderNumId.setText(appointmentService.getOrderNumId());
    }

    @Override
    public int getItemCount() {
        return appointment_services.size();
    }

    public static class DetailAppointmentServiceViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName, servicePrice, category, ownerId, time, orderNumId, userId;

        public DetailAppointmentServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.serviceName);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            category = itemView.findViewById(R.id.serviceCategory);
            userId = itemView.findViewById(R.id.serviceUserId);
            ownerId = itemView.findViewById(R.id.serviceOwnerId);
            time = itemView.findViewById(R.id.serviceTime);
            orderNumId = itemView.findViewById(R.id.orderNumberId);

        }
    }
}
