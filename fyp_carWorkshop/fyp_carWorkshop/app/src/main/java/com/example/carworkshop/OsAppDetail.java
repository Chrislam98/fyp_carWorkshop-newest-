package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsAppDetail extends AppCompatActivity {
    private TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14;
    private List<Owner_service> oswlist;
    AppSerInfoAdapter appSerInfoAdapter;
    private FirebaseAuth firebaseAuth;
    RecyclerView recyclerView3;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_app_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("appointment detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OsAppDetail.this, OwnerAppointmentlistActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        final String userid = getIntent().getStringExtra("userId");
        final String username = getIntent().getStringExtra("username");
        final String userphone = getIntent().getStringExtra("userphone");
        final String appdate = getIntent().getStringExtra("appdate");
        final String apptime = getIntent().getStringExtra("apptime");
        final String cartype = getIntent().getStringExtra("cartype");
        final String carplate = getIntent().getStringExtra("carplate");
        final String status = getIntent().getStringExtra("status");
        final String orderid = getIntent().getStringExtra("orderid");
        final String appnum = getIntent().getStringExtra("appnum");
        final String owid = getIntent().getStringExtra("owid");
        final String oname = getIntent().getStringExtra("oname");
        final String oadd = getIntent().getStringExtra("oadd");
        final String owsname = getIntent().getStringExtra("owsname");

        final Button acc = findViewById(R.id.accbtn);
        final Button complete = findViewById(R.id.complete);
        final Button cancel = findViewById(R.id.cancel);
        final Button back = findViewById(R.id.back);

        t1 = findViewById(R.id.serappcsid);
        t2 = findViewById(R.id.cs2name);
        t3 = findViewById(R.id.phnum2);
        t4 = findViewById(R.id.apptime2);
        t5 = findViewById(R.id.date2);
        t6 = findViewById(R.id.cartype2);
        t7 = findViewById(R.id.carplate2);
        t8 = findViewById(R.id.csstatus2);
        t9 = findViewById(R.id.orid2);
        t10 = findViewById(R.id.apnum);
        t11 = findViewById(R.id.oid);
        t12 = findViewById(R.id.onm);
        t13 = findViewById(R.id.wa);
        t14 = findViewById(R.id.wsn);

        t1.setText(userid);
        t2.setText(username);
        t3.setText(userphone);
        t4.setText(appdate);
        t5.setText(apptime);
        t6.setText(cartype);
        t7.setText(carplate);
        t8.setText(status);
        t9.setText(orderid);
        t10.setText(appnum);
        t11.setText(owid);
        t12.setText(oname);
        t13.setText(oadd);
        t14.setText(owsname);


        recyclerView3 = (RecyclerView) findViewById(R.id.serdeapplist);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        oswlist = new ArrayList<Owner_service>();
        appSerInfoAdapter = new AppSerInfoAdapter(OsAppDetail.this, oswlist);
        recyclerView3.setAdapter(appSerInfoAdapter);
        reference = FirebaseDatabase.getInstance().getReference("Appointment_service").child(userid).child(orderid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Owner_service osw = dataSnapshot1.getValue(Owner_service.class);
                    oswlist.add(osw);
                }
                appSerInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OsAppDetail.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OsAppDetail.this, OwnerAppointmentlistActivity.class));
                }
            });

            if (status.equals("complete") || status.equals("cancel")){
                acc.setVisibility(View.GONE);
                complete.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);

            }
            acc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String at = "accept";
                    final Map<String, Object> map = new HashMap<>();
                    map.put("appointmentDate", t4.getText().toString());
                    map.put("appointmentNum", t10.getText().toString());
                    map.put("appointmentTime", t5.getText().toString());
                    map.put("carBrand", t6.getText().toString());
                    map.put("carPlateNum", t7.getText().toString());
                    map.put("orderNumId", t9.getText().toString());
                    map.put("ownerId", t11.getText().toString());
                    map.put("ownerName", t12.getText().toString());
                    map.put("status", at);
                    map.put("userId", t1.getText().toString());
                    map.put("userPhone", t3.getText().toString());
                    map.put("username", t2.getText().toString());
                    map.put("workshopAddress", t13.getText().toString());
                    map.put("workshopName", t14.getText().toString());
                    String ownerid = t11.getText().toString();
                    final String orderid = t9.getText().toString();
                    final String customerid = t1.getText().toString();

                    FirebaseDatabase.getInstance().getReference("Owner_appointment").child(ownerid).child(orderid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference("Users_appointment").child(customerid).child(orderid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(OsAppDetail.this, OwnerAppointmentlistActivity.class));
                                }
                            });
                        }
                    });
                }
            });

            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cp = "complete";
                    final Map<String, Object> map = new HashMap<>();
                    map.put("appointmentDate", t4.getText().toString());
                    map.put("appointmentNum", t10.getText().toString());
                    map.put("appointmentTime", t5.getText().toString());
                    map.put("carBrand", t6.getText().toString());
                    map.put("carPlateNum", t7.getText().toString());
                    map.put("orderNumId", t9.getText().toString());
                    map.put("ownerId", t11.getText().toString());
                    map.put("ownerName", t12.getText().toString());
                    map.put("status", cp);
                    map.put("userId", t1.getText().toString());
                    map.put("userPhone", t3.getText().toString());
                    map.put("username", t2.getText().toString());
                    map.put("workshopAddress", t13.getText().toString());
                    map.put("workshopName", t14.getText().toString());
                    String ownerid = t11.getText().toString();
                    final String orderid = t9.getText().toString();
                    final String customerid = t1.getText().toString();

                    FirebaseDatabase.getInstance().getReference("Owner_appointment").child(ownerid).child(orderid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference("Users_appointment").child(customerid).child(orderid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(OsAppDetail.this, OwnerAppointmentlistActivity.class));
                                }
                            });
                        }
                    });
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cc = "cancel";
                    final Map<String, Object> map = new HashMap<>();
                    map.put("appointmentDate", t4.getText().toString());
                    map.put("appointmentNum", t10.getText().toString());
                    map.put("appointmentTime", t5.getText().toString());
                    map.put("carBrand", t6.getText().toString());
                    map.put("carPlateNum", t7.getText().toString());
                    map.put("orderNumId", t9.getText().toString());
                    map.put("ownerId", t11.getText().toString());
                    map.put("ownerName", t12.getText().toString());
                    map.put("status", cc);
                    map.put("userId", t1.getText().toString());
                    map.put("userPhone", t3.getText().toString());
                    map.put("username", t2.getText().toString());
                    map.put("workshopAddress", t13.getText().toString());
                    map.put("workshopName", t14.getText().toString());
                    String ownerid = t11.getText().toString();
                    final String orderid = t9.getText().toString();
                    final String customerid = t1.getText().toString();

                    FirebaseDatabase.getInstance().getReference("Owner_appointment").child(ownerid).child(orderid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference("Users_appointment").child(customerid).child(orderid).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(OsAppDetail.this, OwnerAppointmentlistActivity.class));
                                }
                            });
                        }
                    });
                }
            });

        }
}