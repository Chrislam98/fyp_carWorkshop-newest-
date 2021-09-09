package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddVehicleActivity extends AppCompatActivity {

    MaterialEditText carBrand, carColor, carPlatNum;
    Button addVehicle;
    TextView userId;
    ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private UsersData usersData;
//    Pattern pattern = Pattern.compile("[A-Z]{3}[0-9]{4}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Vehicle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddVehicleActivity.this,StartActivity.class));
            }
        });
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        carBrand = findViewById(R.id.carBrand);
        carColor = findViewById(R.id.carColor);
        carPlatNum = findViewById(R.id.carPlateNum);
        userId = findViewById(R.id.userId);
        addVehicle = findViewById(R.id.addVehicle);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersData = dataSnapshot.getValue(UsersData.class);
                assert  usersData != null;
                userId.setText(usersData.getUserId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddVehicleActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String CarBrand = Objects.requireNonNull(carBrand.getText()).toString();
                final String CarColor = Objects.requireNonNull(carColor.getText()).toString();
                final String CarPlatNum = Objects.requireNonNull(carPlatNum.getText()).toString();
                final String UserId = userId.getText().toString();

                if (TextUtils.isEmpty(CarBrand) ||
                        TextUtils.isEmpty(CarColor) ||
                        TextUtils.isEmpty(CarPlatNum)) {
                    Toast.makeText(AddVehicleActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if(CarPlatNum.matches("[A-Z]{3}[0-9]{4}")){
                    add(CarBrand, CarColor, CarPlatNum, UserId);
                } else if(!CarPlatNum.matches("[A-Z]{3}[0-9]{4}")){
                    Toast.makeText(AddVehicleActivity.this, "Please reCorrect Plate Number (ABC1234)", Toast.LENGTH_SHORT).show();
                } else {
                    add(CarBrand, CarColor, CarPlatNum, UserId);
                }
            }
        });


    }

    private void add(String carBrand, String carColor, String carPlatNum, String userId) {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("User_vehicle").child(userId).child(carPlatNum);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("brand", carBrand);
        hashMap.put("color", carColor);
        hashMap.put("plateNum", carPlatNum);
        hashMap.put("userId",userId);
        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(AddVehicleActivity.this, "Your Vehicle Is Added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddVehicleActivity.this,StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(AddVehicleActivity.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}