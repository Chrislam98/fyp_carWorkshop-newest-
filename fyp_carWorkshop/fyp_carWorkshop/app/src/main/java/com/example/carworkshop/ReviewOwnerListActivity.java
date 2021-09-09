package com.example.carworkshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBuffer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewOwnerListActivity extends AppCompatActivity {

    RecyclerView workshopList;
    DatabaseReference databaseReference;
    ReviewShopListAdapter reviewShopListAdapter;
    ArrayList<Owner> owners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_owner_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Review Shop List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReviewOwnerListActivity.this, StartActivity.class));
            }
        });

        workshopList = findViewById(R.id.workshopList);
        workshopList.setLayoutManager(new LinearLayoutManager(this));
        owners = new ArrayList<Owner>();


        databaseReference = FirebaseDatabase.getInstance().getReference("Owners");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    Owner os = dataSnapshot1.getValue(Owner.class);
                    owners.add(os);
                }
                reviewShopListAdapter = new ReviewShopListAdapter(owners);
                workshopList.setAdapter(reviewShopListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReviewOwnerListActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}