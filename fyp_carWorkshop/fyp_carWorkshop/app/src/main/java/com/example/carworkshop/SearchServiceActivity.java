package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchServiceActivity extends AppCompatActivity {

    Query query, query2, query3;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
//    TextView ownerId, category;
    DatabaseReference referenceIcon, referenceService, referenceOwner;

    OwnerAdapter ownerAdapter;
    ArrayList<Owner> ownersList;
    ArrayList<Owner_service> ownerServiceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_service);

        final String iName = getIntent().getStringExtra("iName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Service: " + iName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchServiceActivity.this, UserHomeActivity.class));
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
//        ownerId = findViewById(R.id.ownerId);
//        category = findViewById(R.id.category);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ownersList = new ArrayList<Owner>();
        ownerServiceArrayList = new ArrayList<Owner_service>();

        referenceService = FirebaseDatabase.getInstance().getReference("Owner_service");
        referenceOwner = FirebaseDatabase.getInstance().getReference("Owners");

        query = FirebaseDatabase.getInstance().getReference("Owners");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Toast.makeText(SearchServiceActivity.this, "Got " + iName, Toast.LENGTH_SHORT).show();
                    String ownerId1 = (String) dataSnapshot1.child("ownerId").getValue();

                    query2 = FirebaseDatabase.getInstance().getReference("Owner_service").child(ownerId1).orderByChild("category").equalTo(iName);
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String ownerId2 = (String) dataSnapshot1.child("ownerId").getValue();
                                String category1 = (String) dataSnapshot1.child("category").getValue();

                                query3 = FirebaseDatabase.getInstance().getReference("Owners").orderByChild("ownerId").equalTo(ownerId2);
                                query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                             Owner ownerDataList = dataSnapshot1.getValue(Owner.class);
                                             ownersList.add(ownerDataList);
                                        }
                                        ownerAdapter = new OwnerAdapter(ownersList);
                                        recyclerView.setAdapter(ownerAdapter);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(SearchServiceActivity.this, "NO Owner", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(SearchServiceActivity.this, "No Category", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchServiceActivity.this, "No Owner", Toast.LENGTH_SHORT).show();
            }
        });


//        referenceOwner.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//                        Owner ownerDataList = dataSnapshot1.getValue(Owner.class);
//                        ownersList.add(ownerDataList);
//                    }
//                    ownerAdapter = new OwnerAdapter(ownersList);
//                    recyclerView.setAdapter(ownerAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(SearchServiceActivity.this, "Error", Toast.LENGTH_SHORT).show();
//            }
//        });


    }
}