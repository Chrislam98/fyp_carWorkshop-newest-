package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class DeleteVehicleActivity extends AppCompatActivity {

    String userId = "", plateNum = "";
    TextView userId1, brand1, plateNum1, color;
    Button delete, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_vehicle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeleteVehicleActivity.this, EditVehicleActivity.class));
            }
        });

        userId = getIntent().getStringExtra("userId");
        plateNum = getIntent().getStringExtra("plateNum");

        userId1 = findViewById(R.id.userId);
        brand1 = findViewById(R.id.brand);
        plateNum1 = findViewById(R.id.plateNum);
        color = findViewById(R.id.color);
        delete = findViewById(R.id.delete);
        cancel = findViewById(R.id.cancel);

        getVehicleDetails(userId, plateNum);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("User_vehicle").child(userId).child(plateNum).removeValue();
                Toast.makeText(DeleteVehicleActivity.this, "Vehicle Successful Deleted", Toast.LENGTH_LONG).show();
                startActivity(new Intent(DeleteVehicleActivity.this, StartActivity.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeleteVehicleActivity.this, EditVehicleActivity.class));
            }
        });
    }


    private void getVehicleDetails(String userId, String plateNum) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User_vehicle");
        ref.child(userId).child(plateNum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);
                userId1.setText(vehicle.getUserId());
                brand1.setText(vehicle.getBrand());
                plateNum1.setText(vehicle.getPlateNum());
                color.setText(vehicle.getColor());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DeleteVehicleActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}