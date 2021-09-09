package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsAppointmentActivity extends AppCompatActivity {

    TextView ownerId, workshopName, workshopAddress, appointmentNum, ownerName, ownerEmail, appointmentDate, appointmentTime, carPlateNum, carBrand, status;
    Button goBack, call;
    ImageView imageUrl;
    ImageButton btnMap, btnMapName;
    RecyclerView ownerServices, workshopImage1;
    ArrayList<Appointment> appointments;
    ArrayList<Appointment_service> appointmentService;
    DetailAppointmentServiceAdapter detailAppointmentServiceAdapter;
    WorkshopImageAdapter workshopImageAdapter;
    ArrayList<Icon> wsImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_appointment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Car Shop list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsAppointmentActivity.this, AppointmentListActivity.class));

            }
        });

        final String ownerId1 = getIntent().getStringExtra("ownerId");
        final String userId1 = getIntent().getStringExtra("userId");
        final String orderNumId1 = getIntent().getStringExtra("orderNumId");

        // Details shop
        ownerId = findViewById(R.id.ownerId);
        workshopName = (TextView) findViewById(R.id.shopName);
        workshopAddress = (TextView) findViewById(R.id.shopAddress);
        appointmentNum = (TextView) findViewById(R.id.shopPhone);
        ownerName = (TextView) findViewById(R.id.ownerName);
        ownerEmail = (TextView) findViewById(R.id.ownerEmail);
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentTime = findViewById(R.id.appointmentTime);
        status = findViewById(R.id.status);
        carBrand = findViewById(R.id.carBrand);
        carPlateNum = findViewById(R.id.carPlateNum);
        btnMap = findViewById(R.id.btnMap);
        btnMapName = findViewById(R.id.btnMapName);
        final String appointmentNum1 = appointmentNum.getText().toString();

        // Java Class
        appointments = new ArrayList<Appointment>();
        appointmentService = new ArrayList<Appointment_service>();

        // Display Image
        workshopImage1 = findViewById(R.id.workshopImage1);
        workshopImage1.setLayoutManager(new GridLayoutManager(this,1, RecyclerView.HORIZONTAL,false));

        wsImageList = new ArrayList<Icon>();

        // Details Service
        ownerServices = findViewById(R.id.ownerServices);
        ownerServices.setLayoutManager(new LinearLayoutManager(this));

        getAppointmentDetails(userId1,orderNumId1);
        getAppointmentServiceDetails(userId1,orderNumId1);
        getWorkShopimage(ownerId1);

        btnMapName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopName = workshopName.getText().toString();
                Uri uri = Uri.parse("geo:0, 0?q=" + shopName);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopAddress = workshopAddress.getText().toString();
                Uri uri = Uri.parse("geo:0, 0?q=" + shopAddress);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });


        goBack = findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsAppointmentActivity.this, AppointmentListActivity.class));
            }
        });

        call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + appointmentNum1));
                startActivity(intent);
            }
        });
    }

    private void getWorkShopimage(String ownerId1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ws_Img");
        ref.child(ownerId1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Icon wsImageList1 = dataSnapshot1.getValue(Icon.class);
                    wsImageList.add(wsImageList1);
                }
                workshopImageAdapter = new WorkshopImageAdapter(wsImageList, DetailsAppointmentActivity.this);
                workshopImage1.setAdapter(workshopImageAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getAppointmentServiceDetails(String userId1, String orderNumId1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Appointment_service");
        ref.child(userId1).child(orderNumId1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Appointment_service appointment_service = dataSnapshot1.getValue(Appointment_service.class);
                    appointmentService.add(appointment_service);
                }
                detailAppointmentServiceAdapter = new DetailAppointmentServiceAdapter(DetailsAppointmentActivity.this, appointmentService);
                ownerServices.setAdapter(detailAppointmentServiceAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailsAppointmentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void getAppointmentDetails(String userId1, String orderNumId1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users_appointment");
        ref.child(userId1).child(orderNumId1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Appointment appointment = snapshot.getValue(Appointment.class);
                    assert appointment != null;
                    ownerId.setText(appointment.getOwnerId());
                    workshopName.setText(appointment.getWorkshopName());
                    workshopAddress.setText(appointment.getWorkshopAddress());
                    appointmentNum.setText(appointment.getAppointmentNum());
                    ownerName.setText(appointment.getOwnerName());
                    ownerEmail.setText(appointment.getOwnerEmail());
                    carBrand.setText(appointment.getCarBrand());
                    carPlateNum.setText(appointment.getCarPlateNum());
                    appointmentDate.setText(appointment.getAppointmentDate());
                    appointmentTime.setText(appointment.getAppointmentTime());
                    status.setText(appointment.getStatus());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailsAppointmentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}