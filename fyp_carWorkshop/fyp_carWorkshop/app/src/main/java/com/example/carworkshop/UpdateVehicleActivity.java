package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateVehicleActivity extends AppCompatActivity {

    String userId = "", plateNum = "";
    EditText brand, color;
    TextView plateNum1;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vehicle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vehicle List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateVehicleActivity.this, StartActivity.class));
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userId = getIntent().getStringExtra("userId");
        plateNum = getIntent().getStringExtra("plateNum");

        brand = findViewById(R.id.brand);
        plateNum1 = findViewById(R.id.plateNum);
        color = findViewById(R.id.color);

        Button submit = findViewById(R.id.submit);
        Button cancel = findViewById(R.id.cancel);

        //Vehicle Details
        getVehicleDetails(userId, plateNum);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(brand.getText().toString())) {
                    Toast.makeText(UpdateVehicleActivity.this, "Please Fill Your Car Brand", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(color.getText().toString())) {
                    Toast.makeText(UpdateVehicleActivity.this, "Please Fill Your Car Color", Toast.LENGTH_LONG).show();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("brand", brand.getText().toString());
                    map.put("color", color.getText().toString());

                    FirebaseDatabase.getInstance().getReference("User_vehicle").child(userId).child(plateNum).updateChildren(map).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(UpdateVehicleActivity.this, "Your Vehicle Is Updated", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(UpdateVehicleActivity.this, StartActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateVehicleActivity.this, "Please Required all field", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateVehicleActivity.this, StartActivity.class));
            }
        });
    }

    private void getVehicleDetails(String userId, String plateNum) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User_vehicle");
        ref.child(userId).child(plateNum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);
                assert vehicle != null;
                brand.setText(vehicle.getBrand());
                plateNum1.setText(vehicle.getPlateNum());
                color.setText(vehicle.getColor());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UpdateVehicleActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}