package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Objects;

public class DetailsOwnerActivity extends AppCompatActivity {

    ImageView imageUrl;
    TextView shopName, shopAddress, shopPhone, ownerId;
    Button book, cancel;
    ImageButton map;
    RecyclerView ownerService, ownerReview, carList;
    ArrayList<Owner> owner;
    ArrayList<Owner_service> ownerServiceList;
    ArrayList<Owner_review> ownerReviewList;
    UserReviewOwnerAdapter userReviewOwnerAdapter;
    UserViewOwnerServiceAdapter userViewOwnerServiceAdapter;
    VehicleSelectAdapter vehicleSelectAdapter;
    ArrayList<Vehicle> vehicleArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_owner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Owner Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsOwnerActivity.this, UserHomeActivity.class));
            }
        });

        final String ownerId1 = getIntent().getStringExtra("ownerId");
        final String workshopAddress1 = getIntent().getStringExtra("workshopAddress");
        final String appointmentNum1 = getIntent().getStringExtra("appointmentNum");
        final String workshopName1 = getIntent().getStringExtra("workshopName");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        ownerId = findViewById(R.id.ownerId);
        imageUrl = findViewById(R.id.ownerImage);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        shopPhone = findViewById(R.id.shopPhone);
        map = findViewById(R.id.btnMap);

        book = findViewById(R.id.bookAppointment);
        cancel = findViewById(R.id.cancelBook);

        ownerService = findViewById(R.id.ownerServices);
        ownerService.setLayoutManager(new LinearLayoutManager(this));
        ownerReview = findViewById(R.id.ownerRating);
        ownerReview.setLayoutManager(new LinearLayoutManager(this));
        carList = findViewById(R.id.carList);
        carList.setLayoutManager(new LinearLayoutManager(this));

        ownerServiceList = new ArrayList<Owner_service>();
        ownerReviewList = new ArrayList<Owner_review>();
        vehicleArrayList = new ArrayList<Vehicle>();
        
        getOwnerDetails(ownerId1);
        getOwnerServices(ownerId1);
        getOwnerReview(ownerId1);
        getVehicle(firebaseUser.getUid());

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(shopAddress);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddAppointmentActivity.class);
                intent.putExtra("ownerId", ownerId1);
                intent.putExtra("workshopAddress", workshopAddress1);
                intent.putExtra("workshopName", workshopName1);
                intent.putExtra("appointmentNum", appointmentNum1);
                v.getContext().startActivity(intent);
            }
        });
        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsOwnerActivity.this,UserHomeActivity.class));
            }
        });
        
    }

    private void openMap(TextView shopAddress) {
        String shopAddress1 = shopAddress.getText().toString();
        Uri uri = Uri.parse("geo:0, 0?q=" + shopAddress1);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private void getOwnerReview(String ownerId1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Owner_review");
        ref.child(ownerId1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Owner_review owner_review = dataSnapshot1.getValue(Owner_review.class);
                    ownerReviewList.add(owner_review);
                }
                userReviewOwnerAdapter = new UserReviewOwnerAdapter(DetailsOwnerActivity.this, ownerReviewList);
                ownerReview.setAdapter(userReviewOwnerAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailsOwnerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOwnerServices(String ownerId1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Owner_service");
        ref.child(ownerId1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Owner_service os = dataSnapshot1.getValue(Owner_service.class);
                    ownerServiceList.add(os);
                }
                userViewOwnerServiceAdapter = new UserViewOwnerServiceAdapter(DetailsOwnerActivity.this,ownerServiceList);
                ownerService.setAdapter(userViewOwnerServiceAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailsOwnerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVehicle(String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User_vehicle");
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Vehicle vehicle = dataSnapshot1.getValue(Vehicle.class);
                    vehicleArrayList.add(vehicle);
                }
                vehicleSelectAdapter = new VehicleSelectAdapter(DetailsOwnerActivity.this, vehicleArrayList);
                carList.setAdapter(vehicleSelectAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailsOwnerActivity.this, "Car Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOwnerDetails(String ownerId1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Owners");
        ref.child(ownerId1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Owner owner = snapshot.getValue(Owner.class);
                    assert owner != null;
                    ownerId.setText(owner.getOwnerId());
                    shopAddress.setText(owner.getWorkshopAddress());
                    shopName.setText(owner.getWorkshopName());
                    shopPhone.setText(owner.getAppointmentNum());
                    Picasso.get().load(owner.getImageUrl()).into(imageUrl);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailsOwnerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}