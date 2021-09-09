package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

    String userId = "", plateNum = "", carBrand = "", carBrand2 = "";
    EditText color, brand;
    Spinner brand1;
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
        carBrand = getIntent().getStringExtra("carBrand");

        brand = findViewById(R.id.brand);
        plateNum1 = findViewById(R.id.plateNum);
        color = findViewById(R.id.color);

        Button submit = findViewById(R.id.submit);
        Button cancel = findViewById(R.id.cancel);

        //Vehicle Details
        getVehicleDetails(userId, plateNum);

        brand1 = findViewById(R.id.brand1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (UpdateVehicleActivity.this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.vehicle));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brand1.setAdapter(adapter);
        brand1.setSelection(getIndex(brand1, carBrand));

        brand1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carBrand2 = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(carBrand2) || carBrand2.contains("Select…")) {
                    Toast.makeText(UpdateVehicleActivity.this, "Please Fill Your Car Brand", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(color.getText().toString())) {
                    Toast.makeText(UpdateVehicleActivity.this, "Please Fill Your Car Color", Toast.LENGTH_LONG).show();
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("brand", carBrand2);
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

    private int getIndex(Spinner brand1, String carBrand) {
        for (int i=0; i<brand1.getCount(); i++){
            if (brand1.getItemAtPosition(i).toString().equalsIgnoreCase(carBrand)){
                return i;
            }
        }
        return 0;
    };

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