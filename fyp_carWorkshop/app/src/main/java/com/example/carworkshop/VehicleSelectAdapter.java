package com.example.carworkshop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class VehicleSelectAdapter extends RecyclerView.Adapter <VehicleSelectAdapter.VehicleSelectViewHolder> {

    List<Vehicle> vehicle;
    Context c;
    View view;
    DatabaseReference databaseReference;

    public int selectedPosition = 0;
    private final ArrayList<Integer> selectCheck = new ArrayList<>();

    public VehicleSelectAdapter(Context c, List<Vehicle> vehicle) {
        this.vehicle = vehicle;
        this.c = c;

        for (int i = 0; i < vehicle.size(); i++) {
            selectCheck.add(0);
        }
    }

    @NonNull
    @Override
    public VehicleSelectAdapter.VehicleSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VehicleSelectAdapter.VehicleSelectViewHolder(LayoutInflater.from(c).inflate(R.layout.vehicle_select_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final VehicleSelectViewHolder holder, final int position) {
        holder.brand.setText(vehicle.get(position).getBrand());
        holder.plateNum.setText(vehicle.get(position).getPlateNum());

        //For 1 Checkbox
        holder.checkbox.setChecked(selectCheck.get(position) == 1);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k=0; k<selectCheck.size(); k++) {
                    if(k == position) {
                        selectCheck.set(k,1);
                    } else {
                        selectCheck.set(k,0);
                    }
                }
                notifyDataSetChanged();
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        final String carBrand1 = holder.brand.getText().toString();
        final String plateNum1 = holder.plateNum.getText().toString();

        //For Multiple Checkbox
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.checkbox.setChecked(true);
                    final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
                    assert firebaseUser != null;
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid()).child(currentDate);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("orderNumId", currentDate);
                    hashMap.put("carBrand", carBrand1);
                    hashMap.put("carPlateNum", plateNum1);
                    databaseReference.setValue(hashMap);
                } else {
                    holder.checkbox.setChecked(false);
                    final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
                    assert firebaseUser != null;
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid()).child(currentDate);
                    databaseReference.removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicle.size();
    }

    public static class VehicleSelectViewHolder extends RecyclerView.ViewHolder {

        TextView brand, plateNum;
        CheckBox checkbox;

        public VehicleSelectViewHolder(@NonNull View itemView) {
            super(itemView);

            brand = itemView.findViewById(R.id.carBrand);
            plateNum = itemView.findViewById(R.id.carPlateNum);
            checkbox = itemView.findViewById(R.id.checkboxVehicle);
        }
    }
}
