package com.example.carworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

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
import java.util.Locale;

public class OwnerServiceAdapter extends RecyclerView.Adapter <OwnerServiceAdapter.OwnerServiceViewHolder> {

    View view;
    Context c;
    ArrayList<Owner_service> ownerServiceArrayList;
    ArrayList<Appointment> appointments;
    DatabaseReference databaseReference;
    String orderNumId;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    public OwnerServiceAdapter(Context c, ArrayList<Owner_service> ownerServiceArrayList, String orderNumId) {
        this.c = c;
        this.ownerServiceArrayList = ownerServiceArrayList;
        this.orderNumId = orderNumId;
    }

    @NonNull
    @Override
    public OwnerServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(c).inflate(R.layout.service_list,parent,false);
        return new OwnerServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OwnerServiceViewHolder holder, final int position) {

        final Owner_service ownerService = ownerServiceArrayList.get(position);

        holder.servicePrice.setText(ownerService.getPrice());
        holder.serviceName.setText(ownerService.getServicename());
        holder.category.setText(ownerService.getCategory());
        holder.time.setText(ownerService.getTime());
        holder.serviceDesc.setText(ownerService.getSer_desc());
        holder.ownerId.setText(ownerService.getOwnerId());

        final String serviceName1 = holder.serviceName.getText().toString();
        final String servicePrice1 = holder.servicePrice.getText().toString();
        final String ownerId1 = holder.ownerId.getText().toString();
        final String category1 = holder.category.getText().toString();
        final String time1 = holder.time.getText().toString();

            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.checkbox.setChecked(true);
                        //make appointment no more than 1 min
//                        final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
                        assert firebaseUser != null;
                        databaseReference = FirebaseDatabase.getInstance().getReference("Appointment_service")
                                .child(firebaseUser.getUid()).child(orderNumId);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("orderNumId", orderNumId);
                        hashMap.put("userId", firebaseUser.getUid());
                        hashMap.put("ownerId", ownerId1);
                        hashMap.put("servicename", serviceName1);
                        hashMap.put("price", servicePrice1);
                        hashMap.put("category", category1);
                        hashMap.put("time", time1);
                        databaseReference.child(category1).setValue(hashMap);

                    } else {
                        holder.checkbox.setChecked(false);
//                        final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
                        final String category1 = holder.category.getText().toString();
                        assert firebaseUser != null;
                        databaseReference = FirebaseDatabase.getInstance().getReference("Appointment_service")
                                .child(firebaseUser.getUid()).child(orderNumId).child(category1);
                        databaseReference.removeValue();
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return ownerServiceArrayList.size();
    }

    public static class OwnerServiceViewHolder extends RecyclerView.ViewHolder{

        TextView serviceName, servicePrice, category, ownerId, serviceDesc, time;
        CheckBox checkbox;

        public OwnerServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.serviceName);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            checkbox = itemView.findViewById(R.id.checkbox);

            category = itemView.findViewById(R.id.serviceCategory);
            serviceName = itemView.findViewById(R.id.serviceName);
            ownerId = itemView.findViewById(R.id.serviceOwnerId);
            serviceDesc = itemView.findViewById(R.id.serviceDesc);
            time = itemView.findViewById(R.id.serviceTime);

        }
    }
}
