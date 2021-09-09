package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class AppointmentListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView appointmentList;
    AppointmentListAdapter appointmentListAdapter;
    ArrayList<Owner> ownersList;
    ArrayList<Appointment> appointments;
    DatabaseReference appointment;
    Query reference;
    Spinner spinnerStatus;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppointmentListActivity.this, StartActivity.class));
            }
        });


        spinnerStatus = findViewById(R.id.spinnerStatus);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (AppointmentListActivity.this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.status));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
        spinnerStatus.setOnItemSelectedListener(this);

        appointmentList = findViewById(R.id.appointmentList);
        appointmentList.setLayoutManager(new LinearLayoutManager(this));
        appointments = new ArrayList<>();

        assert firebaseUser != null;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.contains("complete")) {
            reference = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid())
                    .orderByChild("status").equalTo("complete");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    appointments.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Appointment ap = dataSnapshot1.getValue(Appointment.class);
                            appointments.add(ap);
                        }
                        appointmentListAdapter = new AppointmentListAdapter(AppointmentListActivity.this, appointments);
                        appointmentList.setAdapter(appointmentListAdapter);
                    } else {
                        Toast.makeText(AppointmentListActivity.this, "No Complete Appointment", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AppointmentListActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (text.contains("pending")) {
            reference = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid())
                    .orderByChild("status").equalTo("pending");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    appointments.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Appointment ap = dataSnapshot1.getValue(Appointment.class);
                            appointments.add(ap);
                        }
                        appointmentListAdapter = new AppointmentListAdapter(AppointmentListActivity.this, appointments);
                        appointmentList.setAdapter(appointmentListAdapter);
                    } else {
                        Toast.makeText(AppointmentListActivity.this, "No Pending Appointment", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AppointmentListActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (text.contains("accept")) {
            reference = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid())
                    .orderByChild("status").equalTo("accept");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    appointments.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Appointment ap = dataSnapshot1.getValue(Appointment.class);
                            appointments.add(ap);
                        }
                        appointmentListAdapter = new AppointmentListAdapter(AppointmentListActivity.this, appointments);
                        appointmentList.setAdapter(appointmentListAdapter);
                    } else {
                        Toast.makeText(AppointmentListActivity.this, "No Accept Appointment", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AppointmentListActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (text.contains("cancel")) {
            reference = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid())
                    .orderByChild("status").equalTo("cancel");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    appointments.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Appointment ap = dataSnapshot1.getValue(Appointment.class);
                            appointments.add(ap);
                        }
                        appointmentListAdapter = new AppointmentListAdapter(AppointmentListActivity.this, appointments);
                        appointmentList.setAdapter(appointmentListAdapter);
                    } else {
                        Toast.makeText(AppointmentListActivity.this, "No Cancel Appointment", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AppointmentListActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            appointment = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid());

            appointment.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Appointment appointment = dataSnapshot1.getValue(Appointment.class);
                        appointments.add(appointment);
                    }
                    appointmentListAdapter = new AppointmentListAdapter(AppointmentListActivity.this, appointments);
                    appointmentList.setAdapter(appointmentListAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(AppointmentListActivity.this, "Error Appointment DAta", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}