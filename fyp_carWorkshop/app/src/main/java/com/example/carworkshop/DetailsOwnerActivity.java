package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DetailsOwnerActivity extends AppCompatActivity {

    TextView shopName, shopAddress, shopPhone, ownerId;
    TextView sunS1, sunS2, sunS3, sunS4;
    TextView monS1, monS2, monS3, monS4;
    TextView tuesS1, tuesS2, tuesS3, tuesS4;
    TextView wedS1, wedS2, wedS3, wedS4;
    TextView thusS1, thusS2, thusS3, thusS4;
    TextView friS1, friS2, friS3, friS4;
    TextView satS1, satS2, satS3, satS4;

    Button book, cancel;
    ImageButton map;
    FusedLocationProviderClient fusedLocationProviderClient;
    RecyclerView ownerService, ownerReview, carList, workshopImage;
    ArrayList<Owner_service> ownerServiceList;
    ArrayList<Owner_review> ownerReviewList;
    UserReviewOwnerAdapter userReviewOwnerAdapter;
    UserViewOwnerServiceAdapter userViewOwnerServiceAdapter;
    WorkshopImageAdapter workshopImageAdapter;
    VehicleSelectAdapter vehicleSelectAdapter;
    ArrayList<Vehicle> vehicleArrayList;
    ArrayList<Icon> wsImageList;

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
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        shopPhone = findViewById(R.id.shopPhone);
        map = findViewById(R.id.btnMap);

        // Service Time
        monS1 = findViewById(R.id.monS1);
        monS2 = findViewById(R.id.monS2);
        monS3 = findViewById(R.id.monS3);
        monS4 = findViewById(R.id.monS4);

        tuesS1 = findViewById(R.id.tuesS1);
        tuesS2 = findViewById(R.id.tuesS2);
        tuesS3 = findViewById(R.id.tuesS3);
        tuesS4 = findViewById(R.id.tuesS4);

        wedS1 = findViewById(R.id.wedS1);
        wedS2 = findViewById(R.id.wedS2);
        wedS3 = findViewById(R.id.wedS3);
        wedS4 = findViewById(R.id.wedS4);

        thusS1 = findViewById(R.id.thusS1);
        thusS2 = findViewById(R.id.thusS2);
        thusS3 = findViewById(R.id.thusS3);
        thusS4 = findViewById(R.id.thusS4);

        friS1 = findViewById(R.id.friS1);
        friS2 = findViewById(R.id.friS2);
        friS3 = findViewById(R.id.friS3);
        friS4 = findViewById(R.id.friS4);

        satS1 = findViewById(R.id.satS1);
        satS2 = findViewById(R.id.satS2);
        satS3 = findViewById(R.id.satS3);
        satS4 = findViewById(R.id.satS4);

        sunS1 = findViewById(R.id.sunS1);
        sunS2 = findViewById(R.id.sunS2);
        sunS3 = findViewById(R.id.sunS3);
        sunS4 = findViewById(R.id.sunS4);

        book = findViewById(R.id.bookAppointment);
        cancel = findViewById(R.id.cancelBook);


        ownerService = findViewById(R.id.ownerServices);
        ownerService.setLayoutManager(new LinearLayoutManager(this));
        ownerReview = findViewById(R.id.ownerRating);
        ownerReview.setLayoutManager(new LinearLayoutManager(this));
        carList = findViewById(R.id.carList);
        carList.setLayoutManager(new LinearLayoutManager(this));
        workshopImage = findViewById(R.id.workshopImage);
        workshopImage.setLayoutManager(new GridLayoutManager(this,1, RecyclerView.HORIZONTAL,false));

        ownerServiceList = new ArrayList<Owner_service>();
        ownerReviewList = new ArrayList<Owner_review>();
        vehicleArrayList = new ArrayList<Vehicle>();
        wsImageList = new ArrayList<Icon>();
        
        getOwnerDetails(ownerId1);
        getOwnerServices(ownerId1);
        getOwnerReview(ownerId1);
        assert firebaseUser != null;
        getVehicle(firebaseUser.getUid());
        getWorkshopImage(ownerId1);
        getOwnerTime(ownerId1);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(shopAddress);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid()).child(currentDate);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            Intent intent = new Intent(v.getContext(), AddAppointmentActivity.class);
                            intent.putExtra("ownerId", ownerId1);
                            intent.putExtra("workshopAddress", workshopAddress1);
                            intent.putExtra("workshopName", workshopName1);
                            intent.putExtra("appointmentNum", appointmentNum1);
                            intent.putExtra("orderNumId", currentDate);
                            v.getContext().startActivity(intent);
                        } else {
                            Toast.makeText(v.getContext(), "Please Choose ONE(1) Vehicle and Start Booking", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(v.getContext(), "Please Choose ONE(1) Vehicle and Start Booking", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid()).child(currentDate);
//                ref.removeValue();
//                vehicleSelectAdapter.notifyDataSetChanged();
//                startActivity(new Intent(DetailsOwnerActivity.this,UserHomeActivity.class));
                final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid()).child(currentDate);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users_appointment").child(firebaseUser.getUid()).child(currentDate);
                            ref.removeValue();
                            startActivity(new Intent(DetailsOwnerActivity.this,UserHomeActivity.class));

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                vehicleSelectAdapter.notifyDataSetChanged();
            }
        });

    }

    private void getOwnerTime(final String ownerId1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Operation_hrs");
        ref.child(ownerId1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    OwnerTime ownerTime = dataSnapshot.getValue(OwnerTime.class);

                    if(ownerTime.getMonS1().equals("Rest")){
                        monS1.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    monS1.setText(ownerTime.getMonS1());
                    if(ownerTime.getMonS2().equals("Rest")){
                        monS2.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    monS2.setText(ownerTime.getMonS2());
                    if(ownerTime.getMonS3().equals("Rest")){
                        monS3.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    monS3.setText(ownerTime.getMonS3());
                    if(ownerTime.getMonS4().equals("Rest")){
                        monS4.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    monS4.setText(ownerTime.getMonS4());

                    if(ownerTime.getTuesS1().equals("Rest")){
                        tuesS1.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    tuesS1.setText(ownerTime.getTuesS1());
                    if(ownerTime.getTuesS2().equals("Rest")){
                        tuesS2.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    tuesS2.setText(ownerTime.getTuesS2());
                    if(ownerTime.getTuesS3().equals("Rest")){
                        tuesS3.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    tuesS3.setText(ownerTime.getTuesS3());
                    if(ownerTime.getTuesS4().equals("Rest")){
                        tuesS4.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    tuesS4.setText(ownerTime.getTuesS4());

                    if(ownerTime.getWedS1().equals("Rest")){
                        wedS1.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    wedS1.setText(ownerTime.getWedS1());
                    if(ownerTime.getWedS2().equals("Rest")){
                        wedS2.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    wedS2.setText(ownerTime.getWedS2());
                    if(ownerTime.getWedS3().equals("Rest")){
                        wedS3.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    wedS3.setText(ownerTime.getWedS3());
                    if(ownerTime.getWedS4().equals("Rest")){
                        wedS4.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    wedS4.setText(ownerTime.getWedS4());

                    if(ownerTime.getThusS1().equals("Rest")){
                        thusS1.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    thusS1.setText(ownerTime.getThusS1());
                    if(ownerTime.getThusS2().equals("Rest")){
                        thusS2.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    thusS2.setText(ownerTime.getThusS2());
                    if(ownerTime.getThusS3().equals("Rest")){
                        thusS3.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    thusS3.setText(ownerTime.getThusS3());
                    if(ownerTime.getThusS4().equals("Rest")){
                        thusS4.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    thusS4.setText(ownerTime.getThusS4());

                    if(ownerTime.getFriS1().equals("Rest")){
                        friS1.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    friS1.setText(ownerTime.getFriS1());
                    if(ownerTime.getFriS2().equals("Rest")){
                        friS2.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    friS2.setText(ownerTime.getFriS2());
                    if (ownerTime.getFriS3().equals("Rest")){
                        friS3.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    friS3.setText(ownerTime.getFriS3());
                    if (ownerTime.getFriS4().equals("Rest")){
                        friS4.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    friS4.setText(ownerTime.getFriS4());

                    if (ownerTime.getSatS1().equals("Rest")){
                        satS1.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    satS1.setText(ownerTime.getSatS1());
                    if (ownerTime.getSatS2().equals("Rest")){
                        satS2.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    satS2.setText(ownerTime.getSatS2());
                    if (ownerTime.getSatS3().equals("Rest")){
                        satS3.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    satS3.setText(ownerTime.getSatS3());
                    if (ownerTime.getSatS4().equals("Rest")){
                        satS4.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    satS4.setText(ownerTime.getSatS4());

                    if (ownerTime.getSunS1().equals("Rest")){
                        sunS1.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    sunS1.setText(ownerTime.getSunS1());
                    if (ownerTime.getSunS2().equals("Rest")){
                        sunS2.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    sunS2.setText(ownerTime.getSunS2());
                    if (ownerTime.getSunS3().equals("Rest")){
                        sunS3.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    sunS3.setText(ownerTime.getSunS3());
                    if (ownerTime.getSunS4().equals("Rest")){
                        sunS4.setTextColor(ContextCompat.getColor(DetailsOwnerActivity.this, R.color.red));
                    }
                    sunS4.setText(ownerTime.getSunS4());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailsOwnerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWorkshopImage(String ownerId1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ws_Img");
        ref.child(ownerId1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Icon wsImageList1 = dataSnapshot1.getValue(Icon.class);
                    wsImageList.add(wsImageList1);
                }
                workshopImageAdapter = new WorkshopImageAdapter(wsImageList, DetailsOwnerActivity.this);
                workshopImage.setAdapter(workshopImageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailsOwnerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openMap(final TextView shopAddress) {
        String shopAddress1 = shopAddress.getText().toString();

//        Uri uri = Uri.parse("google.navigation:q=" + shopAddress1 + "&avoid=tf");
//        Uri uri = Uri.parse("google.direction:q=" + shopAddress1 + "&mode=d");
        Uri uri = Uri.parse("geo:0, 0?q=" + shopAddress1);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location = task.getResult();
//                if (location != null) {
//                    try {
//                        Geocoder geocoder = new Geocoder(DetailsOwnerActivity.this,
//                                Locale.getDefault());
//                        //Initialize Address List
//                        List<Address> addresses = geocoder.getFromLocation
//                                (location.getLatitude(), location.getLongitude(), 1);
//                        //Set Latitude to String
//                        String latitude = String.valueOf(addresses.get(0).getLatitude());
//                        String longitude = String.valueOf(addresses.get(0).getLongitude());
//
//                        String shopAddress1 = shopAddress.getText().toString();
//
//                        Uri uri = Uri.parse("google.direction:q=" + latitude + "," + longitude + "/" + shopAddress1);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setPackage("com.google.android.apps.maps");
//                        startActivity(intent);
//
//                    } catch (ActivityNotFoundException | IOException e) {
//                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                }
//            }
//        });
//    }

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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailsOwnerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}