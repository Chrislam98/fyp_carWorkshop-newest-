package com.example.carworkshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppUpdateAdapter extends RecyclerView.Adapter<AppUpdateAdapter.MyViewHolder> {

    Context context;
    List<Appointment> appointments;

    public AppUpdateAdapter(Context c1, List<Appointment> ap){
        context = c1;
        appointments = ap;
    }
    @NonNull
    @Override
    public AppUpdateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.applist_info,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppUpdateAdapter.MyViewHolder holder, final int position) {
        holder.csid.setText(appointments.get(position).getUserId());
        holder.csname.setText(appointments.get(position).getUsername());
        holder.csphn.setText(appointments.get(position).getUserPhone());
        holder.appdate.setText(appointments.get(position).getAppointmentDate());
        holder.apptime.setText(appointments.get(position).getAppointmentTime());
        holder.cartype.setText(appointments.get(position).getCarBrand());
        holder.carpalte.setText(appointments.get(position).getCarPlateNum());
        holder.status.setText(appointments.get(position).getStatus());
        holder.orid.setText(appointments.get(position).getOrderNumId());
        holder.appnum.setText(appointments.get(position).getAppointmentNum());
        holder.owid.setText(appointments.get(position).getOwnerId());
        holder.oname.setText(appointments.get(position).getOwnerName());
        holder.oadd.setText(appointments.get(position).getWorkshopAddress());
        holder.owsname.setText(appointments.get(position).getWorkshopName());


        holder.adbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass value to aktivity
                Intent intent = new Intent(v.getContext(), OsAppDetail.class);
                intent.putExtra("userId", appointments.get(position).getUserId());
                intent.putExtra("username", appointments.get(position).getUsername());
                intent.putExtra("userphone",appointments.get(position).getUserPhone());
                intent.putExtra("appdate",appointments.get(position).getAppointmentDate());
                intent.putExtra("apptime",appointments.get(position).getAppointmentTime());
                intent.putExtra("cartype",appointments.get(position).getCarBrand());
                intent.putExtra("carplate",appointments.get(position).getCarPlateNum());
                intent.putExtra("status",appointments.get(position).getStatus());
                intent.putExtra("orderid",appointments.get(position).getOrderNumId());
                intent.putExtra("appnum",appointments.get(position).getAppointmentNum());
                intent.putExtra("owid",appointments.get(position).getOwnerId());
                intent.putExtra("oname",appointments.get(position).getOwnerName());
                intent.putExtra("oadd",appointments.get(position).getWorkshopAddress());
                intent.putExtra("owsname",appointments.get(position).getWorkshopName());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView csname, csphn, appdate, apptime, cartype, carpalte,status,csid,orid,appnum,owid,oname,oadd,owsname;
        Button adbtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            csid = (TextView) itemView.findViewById(R.id.csid);
            csname = (TextView) itemView.findViewById(R.id.cs1name);
            csphn = (TextView) itemView.findViewById(R.id.phnum);
            appdate = (TextView) itemView.findViewById(R.id.date);
            apptime = (TextView) itemView.findViewById(R.id.apptime);
            cartype = (TextView) itemView.findViewById(R.id.cartype1);
            carpalte = (TextView) itemView.findViewById(R.id.carplate1);
            status = (TextView) itemView.findViewById(R.id.csstatus);
            orid = (TextView) itemView.findViewById(R.id.odri);
            appnum = (TextView) itemView.findViewById(R.id.appnum);
            owid = (TextView) itemView.findViewById(R.id.owid);
            oname = (TextView) itemView.findViewById(R.id.oname);
            oadd = (TextView) itemView.findViewById(R.id.oadd);
            owsname = (TextView) itemView.findViewById(R.id.owsname);
            adbtn = (Button)itemView.findViewById(R.id.adbtn);
        }
    }
}
