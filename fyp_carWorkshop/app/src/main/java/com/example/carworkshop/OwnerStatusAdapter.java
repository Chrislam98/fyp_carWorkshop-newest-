package com.example.carworkshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OwnerStatusAdapter extends RecyclerView.Adapter<OwnerStatusAdapter.MyViewHolder> {
    Context context;
    List<Owner> owners;
//   List<Owner> owners;

    public OwnerStatusAdapter(Context c1,List<Owner> os){
        context = c1;
        owners = os;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.owner_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.uname.setText(owners.get(position).getUsername());
        holder.email.setText(owners.get(position).getEmail());
        holder.status.setText(owners.get(position).getStatus());
        holder.osid.setText(owners.get(position).getOwnerId());
        holder.osappnum.setText(owners.get(position).getAppointmentNum());
        holder.osimgurl.setText(owners.get(position).getImageUrl());
        holder.osmobile.setText(owners.get(position).getMobile());
        holder.osrole.setText(owners.get(position).getRole());
        holder.oswsadd.setText(owners.get(position).getWorkshopAddress());
        holder.oswsname.setText(owners.get(position).getWorkshopName());


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.uname.getContext()).setContentHolder(new ViewHolder(R.layout.ownerdialogcontext)).setExpanded(true,1100).create();
                View myview = dialogPlus.getHolderView();
                final TextView name= myview.findViewById(R.id.osdname);
                final TextView email = myview.findViewById(R.id.osdemail);
                final TextView status = myview.findViewById(R.id.osdstatus);
                final TextView  osid = myview.findViewById(R.id.osdownerID);
                final TextView  osappnum = myview.findViewById(R.id.osdappnum);
                final TextView osimgurl = myview.findViewById(R.id.osdimgurl);
                final TextView osmobile = myview.findViewById(R.id.osdmobile);
                final TextView osrole = myview.findViewById(R.id.osdrole);
                final TextView oswsadd = myview.findViewById(R.id.osdwsadd);
                final TextView oswsname = myview.findViewById(R.id.osdwsname);
                Button block = myview.findViewById(R.id.osdblock);
                Button allow = myview.findViewById(R.id.osdallow);

                name.setText(owners.get(position).getUsername());
                email.setText(owners.get(position).getEmail());
                status.setText(owners.get(position).getStatus());
                osid.setText(owners.get(position).getOwnerId());
                osappnum.setText(owners.get(position).getAppointmentNum());
                osimgurl.setText(owners.get(position).getImageUrl());
                osmobile.setText(owners.get(position).getMobile());
                osrole.setText(owners.get(position).getRole());
                oswsadd.setText(owners.get(position).getWorkshopAddress());
                oswsname.setText(owners.get(position).getWorkshopName());


                dialogPlus.show();

                block.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String bk = "block";
                        Map<String,Object> map = new HashMap<>();
                        map.put("appointmentNum",osappnum.getText().toString());
                        map.put("email",email.getText().toString());
                        map.put("imageUrl",osimgurl.getText().toString());
                        map.put("mobile",osmobile.getText().toString());
                        map.put("ownerId",osid.getText().toString());
                        map.put("role",osrole.getText().toString());
                        map.put("status",bk);
                        map.put("username",name.getText().toString());
                        map.put("workshopAddress",oswsadd.getText().toString());
                        map.put("workshopName",oswsname.getText().toString());
                        String ownerId = osid.getText().toString();
                        FirebaseDatabase.getInstance().getReference("Owners").child(ownerId).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialogPlus.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });

                allow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String alw = "allow";
                        Map<String,Object> map = new HashMap<>();
                        map.put("appointmentNum",osappnum.getText().toString());
                        map.put("email",email.getText().toString());
                        map.put("imageUrl",osimgurl.getText().toString());
                        map.put("mobile",osmobile.getText().toString());
                        map.put("ownerId",osid.getText().toString());
                        map.put("role",osrole.getText().toString());
                        map.put("status",alw);
                        map.put("username",name.getText().toString());
                        map.put("workshopAddress",oswsadd.getText().toString());
                        map.put("workshopName",oswsname.getText().toString());
                        String ownerId = osid.getText().toString();

                        FirebaseDatabase.getInstance().getReference("Owners").child(ownerId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialogPlus.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return owners.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
    TextView uname,email,status,osid,osappnum,osimgurl,osmobile,osrole,oswsadd,oswsname;
    Button edit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            uname = (TextView) itemView.findViewById(R.id.osname);
            email = (TextView) itemView.findViewById(R.id.osemail);
            status = (TextView) itemView.findViewById(R.id.osstatus);
            osid = (TextView) itemView.findViewById(R.id.osid);
            osappnum= (TextView) itemView.findViewById(R.id.osappnum);
            osimgurl= (TextView) itemView.findViewById(R.id.osimgurl);
            osmobile= (TextView) itemView.findViewById(R.id.osmobile);
            osrole= (TextView) itemView.findViewById(R.id.osrole);
            oswsadd= (TextView) itemView.findViewById(R.id.oswsadd);
            oswsname= (TextView) itemView.findViewById(R.id.oswsname);
            edit = (Button)itemView.findViewById(R.id.osmstatus);

        }
}


}
