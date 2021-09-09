package com.example.carworkshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.AppointmentListViewHolder> {

    ArrayList<Appointment> appointments;
    DatabaseReference databaseReference, databaseReference1;
    Context c;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    public AppointmentListAdapter(Context c, ArrayList<Appointment> appointments) {
        this.c = c;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppointmentListViewHolder(LayoutInflater.from(c).inflate(R.layout.user_appointment_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentListViewHolder holder, final int position) {

        holder.workshopName.setText(appointments.get(position).getWorkshopName());
        holder.serviceAddress.setText(appointments.get(position).getWorkshopAddress());
        holder.serviceTime.setText(appointments.get(position).getAppointmentTime());
        holder.serviceDate.setText(appointments.get(position).getAppointmentDate());
        holder.orderNumberId.setText(appointments.get(position).getOrderNumId());
        holder.userId.setText(appointments.get(position).getUserId());
        holder.ownerId.setText(appointments.get(position).getOwnerId());
        holder.status.setText(appointments.get(position).getStatus());

        final String ownerId1 = holder.ownerId.getText().toString();
        final String userId1 = holder.userId.getText().toString();
        final String orderNumberId1 = holder.orderNumberId.getText().toString();

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailsAppointmentActivity.class);
                intent.putExtra("orderNumId", appointments.get(position).getOrderNumId());
                intent.putExtra("userId", firebaseUser.getUid());
                intent.putExtra("ownerId", appointments.get(position).getOwnerId());
                v.getContext().startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(c)
                        .setTitle("Delete Appointment")
                        .setMessage("Are you sure you want to delete this Appointment?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                databaseReference = FirebaseDatabase.getInstance().getReference("Users_appointment")
                                        .child(userId1).child(orderNumberId1);
                                databaseReference.removeValue();

                                databaseReference = FirebaseDatabase.getInstance().getReference("Owner_appointment")
                                        .child(ownerId1).child(orderNumberId1);
                                databaseReference.removeValue();

                                databaseReference1 = FirebaseDatabase.getInstance().getReference("Appointment_service")
                                        .child(userId1).child(orderNumberId1);
                                databaseReference1.removeValue();

                                appointments.clear();
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, appointments.size());
                                notifyDataSetChanged();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class AppointmentListViewHolder extends RecyclerView.ViewHolder {

        TextView workshopName, serviceAddress, serviceTime, serviceDate, orderNumberId, status;
        Button view, delete;
        TextView userId, ownerId;

        public AppointmentListViewHolder(@NonNull View itemView) {
            super(itemView);
            workshopName = itemView.findViewById(R.id.workshopName);
            serviceAddress = itemView.findViewById(R.id.serviceAddress);
            serviceTime = itemView.findViewById(R.id.serviceTime);
            serviceDate = itemView.findViewById(R.id.serviceDate);
            orderNumberId = itemView.findViewById(R.id.orderNumberId);
            status = itemView.findViewById(R.id.status);

            userId = itemView.findViewById(R.id.userId);
            ownerId = itemView.findViewById(R.id.ownerId);

            view = itemView.findViewById(R.id.viewService);
            delete = itemView.findViewById(R.id.deleteService);
        }
    }
}
