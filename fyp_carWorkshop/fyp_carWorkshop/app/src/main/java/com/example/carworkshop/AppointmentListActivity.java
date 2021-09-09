package com.example.carworkshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class AppointmentListActivity extends AppCompatActivity {

    RecyclerView appointmentList;
    AppointmentListAdapter appointmentListAdapter;
    ArrayList<Owner> ownersList;
    ArrayList<Appointment> appointments;
    DatabaseReference appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Appointment list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppointmentListActivity.this, StartActivity.class));
            }
        });

        appointmentList = findViewById(R.id.appointmentList);
        appointmentList.setLayoutManager(new LinearLayoutManager(this));
        appointments = new ArrayList<>();

        assert firebaseUser != null;
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