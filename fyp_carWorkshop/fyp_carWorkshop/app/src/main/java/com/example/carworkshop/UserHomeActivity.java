package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHomeActivity extends AppCompatActivity {

    OwnerAdapter adapter;
    private String ownerId = "";
    DatabaseReference reference, reference2;
    ArrayList<Owner> ownersList;
    List<Icon> icon;
    TextView userStatus;
    private UsersData usersData;
    private ProgressBar progressBar1, progressBar2;
    DatabaseReference databaseReference;

    RecyclerView recyclerView, recyclerView1;

    ServiceTypeAdapter serviceTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);

        //initialize View
        recyclerView =  findViewById(R.id.recyclerView);// All Shop
        recyclerView1 = findViewById(R.id.recyclerView1);// Category

        //initialize Value
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));

        ownersList = new ArrayList<Owner>();
        icon = new ArrayList<>();
        serviceTypeAdapter = new ServiceTypeAdapter(this,icon);
        final String role1 = getIntent().getStringExtra("role");

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


        reference = FirebaseDatabase.getInstance().getReference().child("Owners");
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
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Icon icon1 = dataSnapshot1.getValue(Icon.class);
                    icon.add(icon1);
                }
                serviceTypeAdapter = new ServiceTypeAdapter(UserHomeActivity.this,icon);
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
        getMenuInflater().inflate(R.menu.user_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.userProfile) {
            startActivity(new Intent(UserHomeActivity.this,StartActivity.class));
        } else if (id == R.id.search){
            startActivity(new Intent(UserHomeActivity.this,SearchWorkShopActivity.class));
        }
        return true;
    };
}