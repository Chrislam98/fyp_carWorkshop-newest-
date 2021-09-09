package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHomeActivity extends AppCompatActivity {

    OwnerAdapter adapter;
    private String ownerId = "";
    Query reference, reference2;
    ArrayList<Owner> ownersList;
    List<Icon> icon;
    TextView userStatus;
    FusedLocationProviderClient fusedLocationProviderClient;
    private UsersData usersData;
    private ProgressBar progressBar1, progressBar2;
    DatabaseReference databaseReference;

    RecyclerView recyclerView, recyclerView1;

    ServiceTypeAdapter serviceTypeAdapter;

    ImageSlider mainslider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainslider = (ImageSlider)findViewById(R.id.image_slider);
        final List<SlideModel> remoteimages = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("advimg").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    remoteimages.add(new SlideModel(data.child("iUrl").getValue().toString(),data.child("iName").getValue().toString(), ScaleTypes.FIT));
                }
                mainslider.setImageList(remoteimages,ScaleTypes.FIT);

                mainslider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Toast.makeText(getApplicationContext(),remoteimages.get(i).getTitle().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //initialize View
        recyclerView = findViewById(R.id.recyclerView);// All Shop
        recyclerView1 = findViewById(R.id.recyclerView1);// Category

        //initialize Value
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 2));

        ownersList = new ArrayList<Owner>();
        icon = new ArrayList<>();
        serviceTypeAdapter = new ServiceTypeAdapter(this, icon);
        final String role1 = getIntent().getStringExtra("role");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        userStatus = findViewById(R.id.userStatus);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersData = dataSnapshot.getValue(UsersData.class);
                assert usersData != null;

                if (usersData.getStatus().equals("block")) {
                    Intent intent = new Intent(UserHomeActivity.this, LoginUserActivity.class);
                    Toast.makeText(UserHomeActivity.this, "your account has been block ", Toast.LENGTH_LONG).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("Owners").orderByChild("status").equalTo("allow");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        Owner ownerDataList = dataSnapshot1.getValue(Owner.class);
                        ownersList.add(ownerDataList);
                    }
                    adapter = new OwnerAdapter(ownersList);
                    progressBar1.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserHomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        reference2 = FirebaseDatabase.getInstance().getReference().child("ser_icon");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Icon icon1 = dataSnapshot1.getValue(Icon.class);
                    icon.add(icon1);
                }
                serviceTypeAdapter = new ServiceTypeAdapter(UserHomeActivity.this, icon);
                progressBar2.setVisibility(View.GONE);
                recyclerView1.setAdapter(serviceTypeAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserHomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.userProfile) {
            startActivity(new Intent(UserHomeActivity.this, StartActivity.class));
        } else if (id == R.id.search) {
            startActivity(new Intent(UserHomeActivity.this, SearchWorkShopActivity.class));
        } else if (id == R.id.currentLocation) {
            if (ActivityCompat.checkSelfPermission(UserHomeActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // When Permission Granded
                getLocation();
            } else {
                ActivityCompat.requestPermissions(UserHomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
        return true;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize Location
                Location location = task.getResult();
                if(location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(UserHomeActivity.this,
                                Locale.getDefault());
                        //Initialize Address List
                        List<Address> addresses = geocoder.getFromLocation
                                (location.getLatitude(), location.getLongitude(), 1);
                    //Set Latitude to String
                        String latitude = String.valueOf(addresses.get(0).getLatitude());
                        String longitude = String.valueOf(addresses.get(0).getLongitude());

                        //Current location in google map
                        Uri uri = Uri.parse("geo:" + latitude + "," + longitude + "q=workshop");
                        // Current location in Kuala lumpur
//                        Uri uri = Uri.parse("geo: 3.140853, 101.693207");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);

                    } catch (ActivityNotFoundException | IOException e) {
                        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
    };
}