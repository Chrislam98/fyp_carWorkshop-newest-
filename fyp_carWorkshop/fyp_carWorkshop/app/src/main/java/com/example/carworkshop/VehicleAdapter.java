package com.example.carworkshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    Context context;
    ArrayList<Vehicle> vehicle;
    DatabaseReference databaseReference;

    public VehicleAdapter(Context c, ArrayList<Vehicle> v) {
        context = c;
        vehicle = v;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VehicleViewHolder(LayoutInflater.from(context).inflate(R.layout.vehicle_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final VehicleViewHolder holder, final int position) {

        holder.brand.setText(vehicle.get(position).getBrand());
        holder.plateNum.setText(vehicle.get(position).getPlateNum());
        holder.color.setText(vehicle.get(position).getColor());
        holder.userId.setText(vehicle.get(position).getUserId());

        holder.itemView.setTag(vehicle);

        final String userId1 = holder.userId.getText().toString();
        final String plateNum1 = holder.plateNum.getText().toString();

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdateVehicleActivity.class);
                intent.putExtra("userId", vehicle.get(position).getUserId());
                intent.putExtra("plateNum", vehicle.get(position).getPlateNum());
                v.getContext().startActivity(intent);

            }
        });

       holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete Vehicle")
                        .setMessage("Are you sure you want to delete this Vehicle?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                databaseReference = FirebaseDatabase.getInstance().getReference("User_vehicle")
                                        .child(userId1).child(plateNum1);
                                databaseReference.removeValue();

                                vehicle.clear();
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, vehicle.size());
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
        return vehicle.size();
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {

        TextView brand, plateNum, color, userId;
        Button update, delete;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);

            brand = (TextView) itemView.findViewById(R.id.brand);
            plateNum = (TextView) itemView.findViewById(R.id.plateNum);
            color = (TextView) itemView.findViewById(R.id.color);
            userId = (TextView) itemView.findViewById(R.id.userId);

            update = (Button) itemView.findViewById(R.id.update);
            delete = (Button) itemView.findViewById(R.id.delete);

        }
    }
}
