package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnerAppointmentlistActivity extends AppCompatActivity {
    Query reference;
    RecyclerView recyclerView2,recyclerView3;

    private List<Appointment> aplist;
    AppUpdateAdapter appUpdateAdapter;

    private List<Owner_service> oswlist;
    AppSerInfoAdapter appSerInfoAdapter;


    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private Appointment appointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_appointmentlist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("appointment list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerAppointmentlistActivity.this, OwnerHomepageActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        final Button all = findViewById(R.id.all);
        final Button pending = findViewById(R.id.pending);
        final Button accept = findViewById(R.id.accept);
        final Button complete = findViewById(R.id.complete);
        final Button cancel = findViewById(R.id.cancel);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        recyclerView2 = (RecyclerView)findViewById(R.id.myAppRecycler);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        aplist = new ArrayList<Appointment>();
        appUpdateAdapter = new AppUpdateAdapter(OwnerAppointmentlistActivity.this,aplist);
        recyclerView2.setAdapter(appUpdateAdapter);
        reference = FirebaseDatabase.getInstance().getReference("Owner_appointment").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aplist.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Appointment ap = dataSnapshot1.getValue(Appointment.class);
                        aplist.add(ap);
                    }
                    appUpdateAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(OwnerAppointmentlistActivity.this, "No Has Any Appointment Details", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OwnerAppointmentlistActivity.this,"error",Toast.LENGTH_SHORT).show();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Owner_appointment").child(firebaseUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        aplist.clear();
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Appointment ap = dataSnapshot1.getValue(Appointment.class);
                                aplist.add(ap);
                            }
                            appUpdateAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(OwnerAppointmentlistActivity.this,"No All Appointment",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OwnerAppointmentlistActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Owner_appointment").child(firebaseUser.getUid())
                        .orderByChild("status").equalTo("pending");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        aplist.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Appointment ap = dataSnapshot1.getValue(Appointment.class);
                                aplist.add(ap);
                            }
                            appUpdateAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(OwnerAppointmentlistActivity.this, "No Pending Appointment", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OwnerAppointmentlistActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Owner_appointment").child(firebaseUser.getUid()).orderByChild("status").equalTo("accept");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        aplist.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Appointment ap = dataSnapshot1.getValue(Appointment.class);
                                aplist.add(ap);
                            }
                            appUpdateAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(OwnerAppointmentlistActivity.this, "No Accept Appointment", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OwnerAppointmentlistActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Owner_appointment").child(firebaseUser.getUid())
                        .orderByChild("status").equalTo("complete");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        aplist.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Appointment ap = dataSnapshot1.getValue(Appointment.class);
                                aplist.add(ap);
                            }
                            appUpdateAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(OwnerAppointmentlistActivity.this, "No Complete Appointment", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OwnerAppointmentlistActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("Owner_appointment").child(firebaseUser.getUid())
                        .orderByChild("status").equalTo("cancel");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        aplist.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Appointment ap = dataSnapshot1.getValue(Appointment.class);
                                aplist.add(ap);
                            }
                            appUpdateAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(OwnerAppointmentlistActivity.this, "No Cancel Appointment", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OwnerAppointmentlistActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}