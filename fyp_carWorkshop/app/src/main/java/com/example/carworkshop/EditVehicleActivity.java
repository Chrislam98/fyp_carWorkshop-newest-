package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditVehicleActivity extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView vehicleList;
    ArrayList<Vehicle> vehicleArrayList;
    VehicleAdapter vehicleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vehicle List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditVehicleActivity.this, StartActivity.class));
            }
        });
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        vehicleList = (RecyclerView) findViewById(R.id.vehicleList);
        vehicleList.setLayoutManager(new LinearLayoutManager(this));
        vehicleArrayList = new ArrayList<Vehicle>();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        reference = FirebaseDatabase.getInstance().getReference("User_vehicle").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Vehicle vehicle = dataSnapshot1.getValue(Vehicle.class);
                    vehicleArrayList.add(vehicle);
                }
                vehicleAdapter = new VehicleAdapter(EditVehicleActivity.this,vehicleArrayList);
                vehicleList.setAdapter(vehicleAdapter);
                vehicleList.invalidate();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditVehicleActivity.this,"error",Toast.LENGTH_SHORT).show();
            }
        });
        }
}